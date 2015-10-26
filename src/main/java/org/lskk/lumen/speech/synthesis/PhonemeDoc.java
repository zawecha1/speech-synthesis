package org.lskk.lumen.speech.synthesis;

import com.google.common.base.Splitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ceefour on 10/8/15.
 */
public class PhonemeDoc implements Serializable {

    private List<Phoneme> phonemes = new ArrayList<>();

    public PhonemeDoc(List<Phoneme> phonemes) {
        this.phonemes = phonemes;
    }

    public static PhonemeDoc fromPho(String pho) {
        return new PhonemeDoc(Splitter.onPattern("\r?\n").omitEmptyStrings().splitToList(pho)
                .stream().map(Phoneme::fromPhoLine)
                .collect(Collectors.toList()));
    }

    public List<Phoneme> getPhonemes() {
        return phonemes;
    }

    @Override
    public String toString() {
        return phonemes.stream().map(it -> it + "\n").collect(Collectors.joining());
    }
}
