package org.lskk.lumen.speech.expression;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.lskk.lumen.core.AudioObject;
import org.lskk.lumen.core.CommunicateAction;
import org.lskk.lumen.core.EmotionKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@SpringBootApplication
@Profile("communicateApp")
public class CommunicateApp implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(CommunicateApp.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(CommunicateApp.class)
                .profiles("communicateApp")
                .web(false)
                .run(args);
    }

    @Inject
    private ProducerTemplate producer;
    @Inject
    private ToJson toJson;
    @Inject
    private ApplicationContext appCtx;

    @Override
    public void run(String... args) throws Exception {
        Preconditions.checkArgument(args.length >= 1, "Usage: communicate SSML_MARKUP");

        final CommunicateParams params = new CommunicateParams();
        final JCommander jc = new JCommander(params, args);

        final CommunicateAction communicateAction = new CommunicateAction();
        communicateAction.setInLanguage(
                Optional.ofNullable(params.inLanguage).map(Locale::forLanguageTag).orElse(Locale.US));
        communicateAction.setAvatarId(Optional.ofNullable(params.avatarId).orElse("nao1"));
        communicateAction.setEmotionKind(params.emotion);
        communicateAction.setObject(Joiner.on(' ').join(params.objects));
        // TODO: fix this!
        if ("ar-SA".equals(params.inLanguage)) {
            //" اَلسَّلَامُ عَلَيْكُمْ"
            communicateAction.setObject(" اَلسَّلَامُ عَلَيْكُمْ");
        }
        final String speechExpressionUri = "rabbitmq://dummy/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&routingKey=lumen.speech.expression";
        log.info("Sending {} to {} ...", communicateAction, speechExpressionUri);
        producer.sendBody(speechExpressionUri, toJson.mapper.writeValueAsBytes(communicateAction));
        SpringApplication.exit(appCtx);
    }

    @Parameters(commandDescription = "Sends CommunicateAction object to lumen.speech.expression.")
    private static class CommunicateParams {

        @Parameter(names = "-l", description = "Language code, e.g. id-ID")
        private String inLanguage;
        @Parameter(names = "-e", description = "Emotion, e.g. SADNESS")
        private EmotionKind emotion;
        @Parameter(names = "-a", description = "Avatar ID, e.g. nao1")
        private String avatarId;
        @Parameter(description = "Message to say.")
        private List<String> objects = new ArrayList<>();
    }
}
