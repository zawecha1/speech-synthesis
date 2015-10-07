package org.lskk.lumen.speech.expression;

import java.io.Serializable;

/**
 * Created by ceefour on 07/10/2015.
 */
public class WordProsody implements Serializable {
    private SyllableProsody defaultSyllable;
    private SyllableProsody firstSyllable;
    private SyllableProsody middleSyllable;
    private SyllableProsody lastSyllable;

    public SyllableProsody getDefaultSyllable() {
        return defaultSyllable;
    }

    public void setDefaultSyllable(SyllableProsody defaultSyllable) {
        this.defaultSyllable = defaultSyllable;
    }

    public SyllableProsody getFirstSyllable() {
        return firstSyllable;
    }

    public void setFirstSyllable(SyllableProsody firstSyllable) {
        this.firstSyllable = firstSyllable;
    }

    public SyllableProsody getMiddleSyllable() {
        return middleSyllable;
    }

    public void setMiddleSyllable(SyllableProsody middleSyllable) {
        this.middleSyllable = middleSyllable;
    }

    public SyllableProsody getLastSyllable() {
        return lastSyllable;
    }

    public void setLastSyllable(SyllableProsody lastSyllable) {
        this.lastSyllable = lastSyllable;
    }

    @Override
    public String toString() {
        return "WordProsody{" +
                "defaultSyllable=" + defaultSyllable +
                ", firstSyllable=" + firstSyllable +
                ", middleSyllable=" + middleSyllable +
                ", lastSyllable=" + lastSyllable +
                '}';
    }
}
