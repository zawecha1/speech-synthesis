package org.lskk.lumen.speech.synthesis;

import java.io.Serializable;

/**
 * Created by ceefour on 10/8/15.
 */
public class ExpressiveWord implements Serializable {
    private String word;
    private String sampaSyllable;

    public ExpressiveWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSampaSyllable() {
        return sampaSyllable;
    }

    public void setSampaSyllable(String sampaSyllable) {
        this.sampaSyllable = sampaSyllable;
    }

    @Override
    public String toString() {
        return "ExpressiveWord{" +
                "word='" + word + '\'' +
                ", sampaSyllable='" + sampaSyllable + '\'' +
                '}';
    }
}