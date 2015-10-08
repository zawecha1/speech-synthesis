package org.lskk.lumen.speech.synthesis;

import java.io.Serializable;

/**
 * Created by ceefour on 07/10/2015.
 */
public class SyllableProsody implements Serializable {
    private Double defaultDuration;
    private Double consonant1Duration;
    private Double consonant2Duration;
    private Double vocalDuration;
    private Double defaultPitch;
    private Double pitch1;
    private Double pitch2;

    public Double getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(Double defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public Double getConsonant1Duration() {
        return consonant1Duration;
    }

    public void setConsonant1Duration(Double consonant1Duration) {
        this.consonant1Duration = consonant1Duration;
    }

    public Double getConsonant2Duration() {
        return consonant2Duration;
    }

    public void setConsonant2Duration(Double consonant2Duration) {
        this.consonant2Duration = consonant2Duration;
    }

    public Double getVocalDuration() {
        return vocalDuration;
    }

    public void setVocalDuration(Double vocalDuration) {
        this.vocalDuration = vocalDuration;
    }

    public Double getDefaultPitch() {
        return defaultPitch;
    }

    public void setDefaultPitch(Double defaultPitch) {
        this.defaultPitch = defaultPitch;
    }

    public Double getPitch1() {
        return pitch1;
    }

    public void setPitch1(Double pitch1) {
        this.pitch1 = pitch1;
    }

    public Double getPitch2() {
        return pitch2;
    }

    public void setPitch2(Double pitch2) {
        this.pitch2 = pitch2;
    }

    @Override
    public String toString() {
        return "SyllableProsody{" +
                "defaultDuration=" + defaultDuration +
                ", consonant1Duration=" + consonant1Duration +
                ", consonant2Duration=" + consonant2Duration +
                ", vocalDuration=" + vocalDuration +
                ", defaultPitch=" + defaultPitch +
                ", pitch1=" + pitch1 +
                ", pitch2=" + pitch2 +
                '}';
    }
}
