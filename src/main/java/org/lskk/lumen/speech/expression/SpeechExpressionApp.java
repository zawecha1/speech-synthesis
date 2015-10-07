package org.lskk.lumen.speech.expression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("speechExpressionApp")
public class SpeechExpressionApp implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(SpeechExpressionApp.class);

    static {
        log.info("java.library.path = {}", System.getProperty("java.library.path"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpeechExpressionApp.class)
                .profiles("speechExpressionApp")
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
