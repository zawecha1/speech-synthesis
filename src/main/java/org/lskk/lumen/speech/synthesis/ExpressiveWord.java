package org.lskk.lumen.speech.synthesis;

import org.lskk.lumen.speech.synthesis.jpa.KnownWord;

import java.io.Serializable;

/**
 * Created by ceefour on 10/8/15.
 */
public class ExpressiveWord implements Serializable {
    private String word;
    private KnownWord knownWord;
    private String syllablesSampa;

    public ExpressiveWord(String word) {
        this.word = word;
    }

    public ExpressiveWord(String word, KnownWord knownWord) {
        this.word = word;
        this.knownWord = knownWord;
        this.syllablesSampa = knownWord.getSyllablesSampa();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSyllablesSampa() {
        return syllablesSampa;
    }

    public void setSyllablesSampa(String syllablesSampa) {
        this.syllablesSampa = syllablesSampa;
    }

    @Override
    public String toString() {
        return "ExpressiveWord{" +
                "word='" + word + '\'' +
                ", syllablesSampa='" + syllablesSampa + '\'' +
                '}';
    }
}