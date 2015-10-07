package org.lskk.lumen.speech.expression;

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
import java.util.Locale;

@SpringBootApplication
@Profile("communicateApp")
public class CommunicateApp implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(CommunicateApp.class);

    static {
        log.info("java.library.path = {}", System.getProperty("java.library.path"));
    }

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
        final CommunicateAction communicateAction = new CommunicateAction();
        communicateAction.setInLanguage(Locale.US); // TODO: parameterizable
        communicateAction.setAvatarId("nao1"); // TODO: parameterizable
        communicateAction.setEmotionKind(EmotionKind.JOY);
        communicateAction.setObject(args[0]);
        final String speechExpressionUri = "rabbitmq://dummy/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&routingKey=lumen.speech.expression";
        log.info("Sending {} to {} ...", communicateAction, speechExpressionUri);
        producer.sendBody(speechExpressionUri, toJson.mapper.writeValueAsBytes(communicateAction));
        SpringApplication.exit(appCtx);
    }

}
