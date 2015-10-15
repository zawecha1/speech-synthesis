package org.lskk.lumen.speech.synthesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("speechSynthesisApp")
public class SpeechSynthesisApp implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(SpeechSynthesisApp.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpeechSynthesisApp.class)
                .profiles("speechSynthesisApp")
                .web(false)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
