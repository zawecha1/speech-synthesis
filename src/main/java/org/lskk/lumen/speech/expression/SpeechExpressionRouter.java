package org.lskk.lumen.speech.expression;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.lskk.lumen.core.CommunicateAction;
import org.lskk.lumen.core.LumenThing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Locale;
import java.util.Optional;

@Component
@Profile("speechExpressionApp")
public class SpeechExpressionRouter extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(SpeechExpressionRouter.class);
    public static final int SAMPLE_RATE = 16000;
    public static final String FLAC_TYPE = "audio/x-flac";
    private static final DefaultExecutor executor = new DefaultExecutor();

    @Inject
    private Environment env;
    @Inject
    private ToJson toJson;
    @Inject
    private ProducerTemplate producer;

    @Override
    public void configure() throws Exception {
        final String ffmpegExecutable = !new File("/usr/bin/ffmpeg").exists() && new File("/usr/bin/avconv").exists() ? "avconv" : "ffmpeg";
        log.info("libav autodetection result: We will use '{}'", ffmpegExecutable);
        from("rabbitmq://localhost/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&routingKey=lumen.speech.expression")
                .to("log:IN.lumen.speech.expression?showHeaders=true&showAll=true&multiline=true")
                .process(exchange -> {
                    final LumenThing thing = toJson.getMapper().readValue(
                            exchange.getIn().getBody(byte[].class), LumenThing.class);
                    if (thing instanceof CommunicateAction) {
                        final CommunicateAction communicateAction = (CommunicateAction) thing;
                        log.info("Got speech: {}", communicateAction);
                        // TODO: implement
                        final Locale lang = Optional.ofNullable(communicateAction.getInLanguage()).orElse(Locale.US);
                        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                            final CommandLine cmdLine = new CommandLine("espeak");
                            cmdLine.addArgument("-s");
                            cmdLine.addArgument("130");
                            if ("in".equals(lang.getLanguage())) {
                                cmdLine.addArgument("-v");
                                cmdLine.addArgument("mb-id1");
                            } else if ("ar".equals(lang.getLanguage())) {
                                cmdLine.addArgument("-v");
                                cmdLine.addArgument("mb-ar1");
                            }
                            cmdLine.addArgument(communicateAction.getObject());
                            executor.setStreamHandler(new PumpStreamHandler(bos));
                            final int executed;
                            try {
                                executed = executor.execute(cmdLine);
                            } finally {
                                log.info("{}: {}", cmdLine, bos.toString());
                            }
                        }
                    }
                });
    }
}
