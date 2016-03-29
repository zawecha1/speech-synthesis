package org.lskk.lumen.speech.synthesis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * gaya bicara yang sesuai dengan emosi
 * Created by ceefour on 07/10/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="@type")
@JsonSubTypes({
        @JsonSubTypes.Type(name="EmotionProsody", value=EmotionProsody.class),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmotionProsody implements Serializable {
    private String id;
    private String name;
    private WordProsody firstWord;
    private WordProsody secondWord;
    private WordProsody defaultWord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WordProsody getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(WordProsody firstWord) {
        this.firstWord = firstWord;
    }

    public WordProsody getSecondWord() {
        return secondWord;
    }

    public void setSecondWord(WordProsody secondWord) {
        this.secondWord = secondWord;
    }

    public WordProsody getDefaultWord() {
        return defaultWord;
    }

    public void setDefaultWord(WordProsody defaultWord) {
        this.defaultWord = defaultWord;
    }

    @Override
    public String toString() {
        return "EmotionProsody{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", firstWord=" + firstWord +
                ", secondWord=" + secondWord +
                ", defaultWord=" + defaultWord +
                '}';
    }
}
