package org.lskk.lumen.speech.synthesis;

import org.lskk.lumen.core.EmotionKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by ceefour on 07/10/2015.
 */
@Repository
public class EmotionProsodies {
    private static final Logger log = LoggerFactory.getLogger(EmotionProsodies.class);

    @Inject
    private ToJson toJson;

    private Map<EmotionKind, EmotionProsody> emotions = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        final Resource[] resources = new PathMatchingResourcePatternResolver(EmotionProsodies.class.getClassLoader())
                .getResources("classpath:org/lskk/lumen/speech/synthesis/*.jsonld");
        for (Resource res : resources) {
            final EmotionProsody emotionProsody = toJson.getMapper().readValue(res.getURL(), EmotionProsody.class);
            final EmotionKind emotionKind = EmotionKind.valueOf(emotionProsody.getId().toUpperCase());
            log.info("Loaded emotion {} {}", emotionKind, emotionProsody);
            emotions.put(emotionKind, emotionProsody);
        }
        log.info("Loaded {} emotion prosodies: {}", emotions.size(), emotions.keySet());
    }

    public Map<EmotionKind, EmotionProsody> getEmotions() {
        return emotions;
    }

    public Optional<EmotionProsody> getEmotion(EmotionKind kind) {
        return Optional.ofNullable(emotions.get(kind));
    }
}
