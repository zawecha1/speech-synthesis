package org.lskk.lumen.speech.synthesis;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.camel.ProducerTemplate;
import org.lskk.lumen.core.*;
import org.lskk.lumen.core.util.ToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, LiquibaseAutoConfiguration.class,
    DataSourceAutoConfiguration.class})
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
        log.info("Args: {}", (Object) args);
        Preconditions.checkArgument(args.length >= 1, "Usage: communicate SSML_MARKUP");

        final CommunicateParams params = new CommunicateParams();
        final JCommander jc = new JCommander(params, args);

        final CommunicateAction communicateAction = new CommunicateAction();
        communicateAction.setInLanguage(
                Optional.ofNullable(params.inLanguage).map(Locale::forLanguageTag).orElse(Locale.US));
        communicateAction.setAvatarId(Optional.ofNullable(params.avatarId).orElse("nao1"));
        communicateAction.setEmotionKind(params.emotion);
        communicateAction.setGender(params.gender);
        communicateAction.setObject(Joiner.on(' ').join(params.objects));
        // TODO: fix this!
        if ("ar-SA".equals(params.inLanguage)) {
            //" اَلسَّلَامُ عَلَيْكُمْ"
            communicateAction.setObject(" اَلسَّلَامُ عَلَيْكُمْ");
        }
        final String speechSynthesisUri = "rabbitmq://dummy/amq.topic?connectionFactory=#amqpConnFactory&exchangeType=topic&autoDelete=false&skipQueueDeclare=true&routingKey=" + LumenChannel.SPEECH_SYNTHESIS.key();
        log.info("Sending {} to {} ...", communicateAction, speechSynthesisUri);
        final byte[] resultJson = producer.requestBody(speechSynthesisUri, toJson.getMapper().writeValueAsBytes(communicateAction), byte[].class);
        final Status status = toJson.getMapper().readValue(resultJson, Status.class);
        log.info("Status: {}", status);
        SpringApplication.exit(appCtx);
    }

    @Parameters(commandDescription = "Sends CommunicateAction object to lumen.speech.synthesis.")
    private static class CommunicateParams {

        @Parameter(names = "-l", description = "Language code, e.g. id-ID")
        private String inLanguage;
        @Parameter(names = "-e", description = "Emotion kind, e.g. SADNESS")
        private EmotionKind emotion;
        @Parameter(names = "-a", description = "Avatar ID, e.g. nao1")
        private String avatarId;
        @Parameter(names = "-g", description = "Gender: MALE | FEMALE")
        private Gender gender;
        @Parameter(description = "Message to say.")
        private List<String> objects = new ArrayList<>();
    }
}
