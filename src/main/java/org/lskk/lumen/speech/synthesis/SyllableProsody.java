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
    private Double firstPitch;
    private Double lastPitch;

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

    public Double getFirstPitch() {
        return firstPitch;
    }

    public void setFirstPitch(Double firstPitch) {
        this.firstPitch = firstPitch;
    }

    public Double getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(Double lastPitch) {
        this.lastPitch = lastPitch;
    }

    @Override
    public String toString() {
        return "SyllableProsody{" +
                "defaultDuration=" + defaultDuration +
                ", consonant1Duration=" + consonant1Duration +
                ", consonant2Duration=" + consonant2Duration +
                ", vocalDuration=" + vocalDuration +
                ", firstPitch=" + firstPitch +
                ", lastPitch=" + lastPitch +
                '}';
    }
}
