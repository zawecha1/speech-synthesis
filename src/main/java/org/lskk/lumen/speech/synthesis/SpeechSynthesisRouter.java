package org.lskk.lumen.speech.synthesis;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.LoggingErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.joda.time.DateTime;
import org.lskk.lumen.core.*;
import org.lskk.lumen.core.util.AsError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

@Component
@Profile("speechSynthesisApp")
public class SpeechSynthesisRouter extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(SpeechSynthesisRouter.class);
    public static final int SAMPLE_RATE = 16000;
    private static final DefaultExecutor executor = new DefaultExecutor();
    private static final File LINUX_MBROLA_SHARE_FOLDER = new File("/usr/share/mbrola");
    private static final File mbrolaShareFolder = LINUX_MBROLA_SHARE_FOLDER.exists() ? LINUX_MBROLA_SHARE_FOLDER : new File("C:/mbroladb");

    @Inject
    private Environment env;
    @Inject
    private ToJson toJson;
    @Inject
    private AsError asError;
    @Inject
    private ProducerTemplate producer;
    @Inject
    private SpeechProsody speechProsody;
    @Inject
    private EmotionProsodies emotionProsodies;

    private String ffmpegExecutable;

    @PostConstruct
    public void init() {
        ffmpegExecutable = !new File("/usr/bin/ffmpeg").exists() && new File("/usr/bin/avconv").exists() ? "avconv" :
                env.getRequiredProperty("ffmpeg.bin");
        log.info("libav autodetection result: We will use '{}'", ffmpegExecutable);
    }

    protected Status processCommunicateAction(final Exchange exchange, final CommunicateAction communicateAction) throws IOException {
        final EmotionKind emotionKind = Optional.ofNullable(communicateAction.getEmotionKind()).orElse(EmotionKind.NEUTRAL);
        final Locale lang = Optional.ofNullable(communicateAction.getInLanguage()).orElse(Locale.US);
        log.info("Got speech lang-legacy={}: {}", lang.getLanguage(), communicateAction);
        final String avatarId = Optional.ofNullable(communicateAction.getAvatarId()).orElse("nao1");

        //final File wavFile = File.createTempFile("lumen-speech-synthesis_", ".wav");
//                        final File oggFile = File.createTempFile("lumen-speech-synthesis_", ".ogg");
        try {
            final byte[] wavBytes;
            if ("in".equals(lang.getLanguage())) {
                // Expressive speech (for now, Indonesian only)
                final PhonemeDoc phonemeDoc;
                if (EmotionKind.NEUTRAL == emotionKind) {
                    phonemeDoc = speechProsody.performNeutral(communicateAction.getObject());
                } else {
                    final EmotionProsody emotionProsody = emotionProsodies.getEmotion(emotionKind)
                            .orElseThrow(() -> new SpeechSynthesisException("Emotion " + emotionKind + " not supported"));
                    phonemeDoc = speechProsody.perform(communicateAction.getObject(), emotionProsody);
                }

                try (final ByteArrayInputStream objectIn = new ByteArrayInputStream(phonemeDoc.toString().getBytes(StandardCharsets.UTF_8));
                     final ByteArrayOutputStream wavStream = new ByteArrayOutputStream();
                     final ByteArrayOutputStream err = new ByteArrayOutputStream()) {
                    final CommandLine cmdLine = new CommandLine("mbrola");
                    cmdLine.addArgument(new File(mbrolaShareFolder, "id1/id1").toString());
                    cmdLine.addArgument("-");
                    cmdLine.addArgument("-.wav");
                    executor.setStreamHandler(new PumpStreamHandler(wavStream, err, objectIn));
                    final int executed;
                    try {
                        executed = executor.execute(cmdLine);
                        wavBytes = wavStream.toByteArray();
                    } finally {
                        log.info("{}: {}", cmdLine, err.toString());
                    }
                }
            } else {
                // Neutral speech
                try (final ByteArrayInputStream objectIn = new ByteArrayInputStream(communicateAction.getObject().getBytes(StandardCharsets.UTF_8));
                     final ByteArrayOutputStream wavStream = new ByteArrayOutputStream();
                     final ByteArrayOutputStream err = new ByteArrayOutputStream()) {
                    final CommandLine cmdLine = new CommandLine("espeak");
                    cmdLine.addArgument("-b");
                    cmdLine.addArgument("1"); // UTF-8
                    cmdLine.addArgument("-m"); // SSML markup
                    cmdLine.addArgument("-s");
                    cmdLine.addArgument("130");
                    if ("in".equals(lang.getLanguage())) {
                        cmdLine.addArgument("-v");
                        cmdLine.addArgument(SpeechProsody.MBROLA_ID1_VOICE);
                    } else if ("ar".equals(lang.getLanguage())) {
                        cmdLine.addArgument("-v");
                        cmdLine.addArgument(SpeechProsody.MBROLA_AR1_VOICE);
                    }
//                                cmdLine.addArgument("-w");
//                                cmdLine.addArgument(wavFile.toString());
                    cmdLine.addArgument("--stdin");
                    cmdLine.addArgument("--stdout");
                    //cmdLine.addArgument(communicateAction.getObject());
                    executor.setStreamHandler(new PumpStreamHandler(wavStream, err, objectIn));
                    final int executed;
                    try {
                        executed = executor.execute(cmdLine);
                        wavBytes = wavStream.toByteArray();
                    } finally {
                        log.info("{}: {}", cmdLine, err.toString());
                    }
                }
            }

            log.info("espeak/mbrola generated {} bytes WAV", wavBytes.length);
            try (final ByteArrayInputStream wavIn = new ByteArrayInputStream(wavBytes);
                 final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 final ByteArrayOutputStream err = new ByteArrayOutputStream()) {
                // flac.exe doesn't support mp3, and that's a problem for now (note: mp3 patent is expiring)
                final CommandLine cmdLine = new CommandLine(ffmpegExecutable);
                cmdLine.addArgument("-i");
                cmdLine.addArgument("-"); //cmdLine.addArgument(wavFile.toString());
                cmdLine.addArgument("-ar");
                cmdLine.addArgument(String.valueOf(SAMPLE_RATE));
                cmdLine.addArgument("-ac");
                cmdLine.addArgument("1");
                cmdLine.addArgument("-f");
                cmdLine.addArgument("ogg");
                // without this you'll get FLAC instead, which browsers do not support
                cmdLine.addArgument("-acodec");
                cmdLine.addArgument("libvorbis");
//                                cmdLine.addArgument("-y"); // happens, weird!
//                                cmdLine.addArgument(oggFile.toString());
                cmdLine.addArgument("-");
                executor.setStreamHandler(new PumpStreamHandler(bos, err, wavIn));
                final int executed;
                try {
                    executed = executor.execute(cmdLine);
                } finally {
                    log.info("{}: {}", cmdLine, err.toString());
                }
//                                Preconditions.checkState(oggFile.exists(), "Cannot convert %s bytes WAV to OGG",
//                                        wavBytes.length);

                // Send
//                                final byte[] audioContent = FileUtils.readFileToByteArray(oggFile);
                final byte[] audioContent = bos.toByteArray();
                final String audioContentType = "audio/ogg";

                final AudioObject audioObject = new AudioObject();
                audioObject.setTranscript(communicateAction.getObject());
                audioObject.setInLanguage(lang);
                audioObject.setMediaLayer(MediaLayer.SPEECH);
                audioObject.setContentType(audioContentType + "; rate=" + SAMPLE_RATE);
                audioObject.setContentUrl("data:" + audioContentType + ";base64," + Base64.encodeBase64String(audioContent));
                audioObject.setContentSize((long) audioContent.length);
//                                audioObject.setName(FilenameUtils.getName(oggFile.getName()));
                audioObject.setName("lumen-speech-" + new DateTime() + ".ogg");
                audioObject.setDateCreated(new DateTime());
                audioObject.setDatePublished(audioObject.getDateCreated());
                audioObject.setDateModified(audioObject.getDateCreated());
                audioObject.setUploadDate(audioObject.getDateCreated());
                final String audioOutUri = "rabbitmq://dummy/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&skipQueueDeclare=true&routingKey=" + AvatarChannel.AUDIO_OUT.key(avatarId);
                log.info("Sending {} to {} ...", audioObject, audioOutUri);
                producer.sendBody(audioOutUri, toJson.mapper.writeValueAsBytes(audioObject));
            }
        } finally {
//                            oggFile.delete();
            //wavFile.delete();
        }

        // reply
        log.trace("Exchange {} is {}", exchange.getIn().getMessageId(), exchange.getPattern());
        final Status status = new Status();
        exchange.getOut().setBody(status);
        return status;
//                        final String replyTo = exchange.getIn().getHeader("rabbitmq.REPLY_TO", String.class);
//                        if (replyTo != null) {
//                            log.debug("Sending reply to {} ...", replyTo);
//                            exchange.getOut().setHeader("rabbitmq.ROUTING_KEY", replyTo);
//                            exchange.getOut().setHeader("rabbitmq.EXCHANGE_NAME", "");
//                            exchange.getOut().setHeader("recipients",
//                                    "rabbitmq://dummy/dummy?connectionFactory=#amqpConnFactory&autoDelete=false,log:OUT." + LumenChannel.SPEECH_SYNTHESIS);
//                        } else {
//                            exchange.getOut().setHeader("recipients");
//                        }
    }

    @Override
    public void configure() throws Exception {
        onException(Exception.class).bean(asError).bean(toJson).handled(true);
        errorHandler(new LoggingErrorHandlerBuilder(log));

        // lumen.speech.synthesis
        from("rabbitmq://localhost/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&queue=speech-synthesis:" + LumenChannel.SPEECH_SYNTHESIS.key() + "&routingKey=" + LumenChannel.SPEECH_SYNTHESIS.key())
                .to("log:IN." + LumenChannel.SPEECH_SYNTHESIS.key() + "?showHeaders=true&showAll=true&multiline=true")
                .process(exchange -> {
                    final LumenThing thing = toJson.getMapper().readValue(
                            exchange.getIn().getBody(byte[].class), LumenThing.class);
                    if (thing instanceof CommunicateAction) {
                        final CommunicateAction communicateAction = (CommunicateAction) thing;
                        processCommunicateAction(exchange, communicateAction);
                    } else {
                        // do not reply
                        exchange.getOut().setBody(null);
                    }
                })
                .bean(toJson);
                //.to("log:OUT." + LumenChannel.SPEECH_SYNTHESIS);
        // avatar.*.chat.outbox
        from("rabbitmq://localhost/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&queue=speech-synthesis:" + AvatarChannel.CHAT_OUTBOX.wildcard() + "&routingKey=" + AvatarChannel.CHAT_OUTBOX.wildcard())
                .to("log:" + SpeechSynthesisRouter.class.getName() + "." + AvatarChannel.CHAT_OUTBOX.wildcard() + "?level=DEBUG&showHeaders=true&showAll=true&showBody=false&multiline=true")
                .process(exchange -> {
                    final String avatarId = AvatarChannel.getAvatarId((String) exchange.getIn().getHeader(RabbitMQConstants.ROUTING_KEY));
                    final LumenThing thing = toJson.getMapper().readValue(
                            exchange.getIn().getBody(byte[].class), LumenThing.class);
                    if (thing instanceof CommunicateAction) {
                        final CommunicateAction communicateAction = (CommunicateAction) thing;
                        if (Boolean.TRUE == communicateAction.getUsedForSynthesis()) {
                            processCommunicateAction(exchange, communicateAction);
                        } else {
                            // do not reply
                            exchange.getOut().setBody(null);
                        }
                    }
                    // we never reply anyway
                    exchange.setPattern(ExchangePattern.InOnly);
                });
    }
}
