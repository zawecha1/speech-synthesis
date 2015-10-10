package org.lskk.lumen.speech.synthesis;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.lskk.lumen.speech.synthesis.jpa.KnownWord;
import org.lskk.lumen.speech.synthesis.jpa.KnownWordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ceefour on 10/8/15.
 */
@Service
@Profile("speechSynthesisApp")
public class SpeechProsody {

    public static final String MBROLA_ID1_VOICE = "mb-id1";
    public static final String MBROLA_AR1_VOICE = "mb-ar1";
    public static final Set<String> VOCALS = ImmutableSet.of("V", "e", "@", "I", "Q", "U", "aI", "OI", "aU");

    private static final Logger log = LoggerFactory.getLogger(SpeechProsody.class);
    private static final DefaultExecutor executor = new DefaultExecutor();

    @Inject
    private KnownWordRepository knownWordRepo;

    /**
     * v1.0 code:
     * <p/>
     * string result = CreatePho(sentence);
     * int totalId = InsertPho(result);
     * <p/>
     * List<String> words = sentence.Split(' ').ToList();
     * List<String> sampaSyllable = SearchSampaSyllable(words);
     * <p/>
     * // check emotion
     * if (radioHappy.Checked)
     * {
     * List<double> values = ReadHappy();
     * Emotion(sampaSyllable, values);
     * }
     * else if (radioAngry.Checked)
     * {
     * List<double> values = ReadAngry();
     * Emotion(sampaSyllable, values);
     * }
     * else if (radioSad.Checked)
     * {
     * List<double> values = ReadSad();
     * Emotion(sampaSyllable, values);
     * }
     * <p/>
     * <p/>
     * string file = "";
     * for (int id = 1; id < totalId+1; id++)
     * {
     * file += GetPhoSampa(id) + "\t";
     * file += GetPhoDuration(id) + "\t";
     * file += GetPhoPitch(id);
     * file = file.Trim();
     * file += "\n";
     * }
     * <p/>
     * <p/>
     * string path = @"C:\OUTPUT-PHO\hasil.pho";
     * File.WriteAllText(path, file);
     * <p/>
     * <p/>
     * DeletePho();
     */

    protected String createPho(String sentence, String voiceId) {
        final String phoStr;
        try (final ByteArrayInputStream objectIn = new ByteArrayInputStream(sentence.getBytes(StandardCharsets.UTF_8));
             final ByteArrayOutputStream phoOut = new ByteArrayOutputStream();
             final ByteArrayOutputStream err = new ByteArrayOutputStream()) {
            final CommandLine cmdLine = new CommandLine("espeak");
            cmdLine.addArgument("-b");
            cmdLine.addArgument("1"); // UTF-8
            cmdLine.addArgument("-m"); // SSML markup
//            cmdLine.addArgument("-s");
//            cmdLine.addArgument("130");
            cmdLine.addArgument("-v");
            cmdLine.addArgument(voiceId);
            cmdLine.addArgument("-q");
            cmdLine.addArgument("--pho");
            cmdLine.addArgument("--stdin");
            cmdLine.addArgument("--stdout");
            executor.setStreamHandler(new PumpStreamHandler(phoOut, err, objectIn));
            final int executed;
            try {
                executed = executor.execute(cmdLine);
                phoStr = new String(phoOut.toByteArray(), StandardCharsets.UTF_8);
            } finally {
                log.info("{}: {}", cmdLine, err.toString());
            }
        } catch (Exception e) {
            throw new SpeechSynthesisException(e, "Cannot generate PHO file for %s sentence: %s",
                    voiceId, sentence);
        }
        return phoStr;
    }

    protected PhonemeDoc insertPho(String pho) {
        return PhonemeDoc.fromPho(pho);
    }

    public PhonemeDoc perform(String sentence, EmotionProsody emotionProsody) {
        final String result = createPho(sentence, MBROLA_ID1_VOICE);
        final PhonemeDoc phonemeDoc = insertPho(result);
        log.info("Original phonemes for '{}': {}", sentence, phonemeDoc);
        final List<String> words = Splitter.on(' ').omitEmptyStrings().splitToList(sentence);
        final List<ExpressiveWord> expressiveWords = searchSampaSyllable(words);
        applyEmotion(expressiveWords, emotionProsody, phonemeDoc);
        log.info("Expressive phonemes for '{}' using {}: {}", sentence, emotionProsody.getId(), phonemeDoc);
        return phonemeDoc;
    }

    /**
     *
     * @param expressiveWords
     * @param emotionProsody
     * @param phonemeDoc Will be modified based on {@link EmotionProsody}.
     */
    protected void applyEmotion(List<ExpressiveWord> expressiveWords, EmotionProsody emotionProsody, PhonemeDoc phonemeDoc) {
        int phonemeIdx = 0;
        for (int wordIdx = 0; wordIdx < expressiveWords.size(); wordIdx++) {
            final WordProsody wordProsody;
            if (wordIdx == 0) {
                wordProsody = emotionProsody.getFirstWord();
            } else if (wordIdx == 1) {
                wordProsody = emotionProsody.getSecondWord();
            } else {
                wordProsody = emotionProsody.getDefaultWord();
            }
            final ExpressiveWord expressiveWord = expressiveWords.get(wordIdx);
            final List<String> syllableSampas = Splitter.on('-').splitToList(expressiveWord.getSyllablesSampa());
            for (int syllableIdx = 0; syllableIdx < syllableSampas.size(); syllableIdx++) {
                final SyllableProsody syllableProsody;
                if (syllableIdx == 0 && wordProsody.getFirstSyllable() != null) {
                    syllableProsody = wordProsody.getFirstSyllable();
                } else if (syllableIdx < syllableSampas.size() - 1 && wordProsody.getMiddleSyllable() != null) {
                    syllableProsody = wordProsody.getMiddleSyllable();
                } else if (wordProsody.getLastSyllable() != null) {
                    syllableProsody = wordProsody.getMiddleSyllable();
                } else {
                    syllableProsody = wordProsody.getDefaultSyllable();
                }
                final String syllableSampa = syllableSampas.get(syllableIdx);
                byte consonantIndex = 0;
                final List<String> letters = Splitter.on('.').splitToList(syllableSampa);
                for (final String letter : letters) {
                    // get the phoneme
                    while (letter.equals(phonemeDoc.getPhonemes().get(phonemeIdx))) {
                        phonemeIdx++;
                    }
                    final Phoneme phoneme = phonemeDoc.getPhonemes().get(phonemeIdx);

                    if (VOCALS.contains(letter)) {
                        // change duration
                        if (syllableProsody.getVocalDuration() != null) {
                            phoneme.setDuration((short) Math.round(phoneme.getDuration() * ((100 + syllableProsody.getVocalDuration()) / 100d)));
                        } else if (syllableProsody.getDefaultDuration() != null) {
                            phoneme.setDuration((short) Math.round(phoneme.getDuration() * ((100 + syllableProsody.getDefaultDuration()) / 100d)));
                        }
                        // change pitch
                        if (syllableProsody.getFirstPitch() != null) {
                            final Byte pct = Iterables.getFirst(phoneme.getPitches().keySet(), null);
                            final Short pitch = phoneme.getPitches().get(pct);
                            phoneme.getPitches().put(pct, (short) Math.round(pitch * ((100 + syllableProsody.getFirstPitch()) / 100d)));
                        }
                        if (syllableProsody.getLastPitch() != null) {
                            final Byte pct = Iterables.getLast(phoneme.getPitches().keySet());
                            final Short pitch = phoneme.getPitches().get(pct);
                            phoneme.getPitches().put(pct, (short) Math.round(pitch * ((100 + syllableProsody.getLastPitch()) / 100d)));
                        }
                    } else { // Consonant
                        if (consonantIndex == 0 && syllableProsody.getConsonant1Duration() != null) {
                            phoneme.setDuration((short) Math.round(phoneme.getDuration() * ((100 + syllableProsody.getConsonant1Duration()) / 100d)));
                        } else if (consonantIndex == 1 && syllableProsody.getConsonant2Duration() != null) {
                            phoneme.setDuration((short) Math.round(phoneme.getDuration() * ((100 + syllableProsody.getConsonant2Duration()) / 100d)));
                        }
                        // other consonants are not modified
                        consonantIndex++;
                    }
                }
            }
        }
    }

    protected List<ExpressiveWord> searchSampaSyllable(List<String> words) {
        return words.stream().map(it -> {
            final String wordLower = it.toLowerCase();
            final KnownWord knownWord = Preconditions.checkNotNull(knownWordRepo.findOneByWord(wordLower),
                    "Unknown word: %s", wordLower);
            return new ExpressiveWord(it, knownWord);
        }).collect(Collectors.toList());
    }

}
