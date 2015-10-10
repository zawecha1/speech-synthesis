package org.lskk.lumen.speech.synthesis;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ceefour on 10/8/15.
 */
public class Phoneme implements Serializable {

    private String sampa;
    private short duration;
    private Map<Byte, Short> pitches = new LinkedHashMap<>();

    public Phoneme(String sampa, short duration, Map<Byte, Short> pitches) {
        this.sampa = sampa;
        this.duration = duration;
        this.pitches = new LinkedHashMap<>(pitches);
    }

    /**
     * SAMPA IPA.
     * @return
     */
    public String getSampa() {
        return sampa;
    }

    public void setSampa(String sampa) {
        this.sampa = sampa;
    }

    /**
     * Duration in milliseconds.
     * @return
     */
    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    /**
     * Pitches, key is percentage, value is pitch value (Hz).
     * @return
     */
    public Map<Byte, Short> getPitches() {
        return pitches;
    }

    public static Phoneme fromPhoLine(String line) {
        final List<String> els = Splitter.on(CharMatcher.anyOf("\t ")).omitEmptyStrings().splitToList(line);
        try {
            final ImmutableMap.Builder<Byte, Short> pitcheb = ImmutableMap.builder();
            for (int i = 2; i < els.size(); i += 2) {
                pitcheb.put(Byte.valueOf(els.get(i)), Short.valueOf(els.get(i + 1)));
            }
            return new Phoneme(els.get(0), Short.valueOf(els.get(1)), pitcheb.build());
        } catch (Exception e) {
            throw new SpeechSynthesisException(e, "Cannot parse %s from '%s'", els, line);
        }
    }

    @Override
    public String toString() {
        String s = "";
        s += getSampa() + '\t' + getDuration();
        if (!getPitches().isEmpty()) {
            s += '\t' + getPitches().entrySet().stream().map(it -> it.getKey() + " " + it.getValue())
                    .collect(Collectors.joining(" "));
        }
        return s;
    }
}
