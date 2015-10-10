package org.lskk.lumen.speech.synthesis;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.lskk.lumen.speech.synthesis.jpa.KnownWord;
import org.lskk.lumen.speech.synthesis.jpa.KnownWordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ceefour on 10/8/15.
 */
@Service
public class SpeechProsody {

    public static final String MBROLA_ID1_VOICE = "mb-id1";
    public static final String MBROLA_AR1_VOICE = "mb-ar1";

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
        final List<String> words = Splitter.on(' ').omitEmptyStrings().splitToList(sentence);
        final List<ExpressiveWord> expressiveWords = searchSampaSyllable(words);
        applyEmotion(expressiveWords, emotionProsody);
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    protected void applyEmotion(List<ExpressiveWord> expressiveWords, EmotionProsody emotionProsody) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    protected List<ExpressiveWord> searchSampaSyllable(List<String> words) {
        return words.stream().map(it -> {
            final KnownWord knownWord = Preconditions.checkNotNull(knownWordRepo.findOneByWord(it),
                    "Unknown word: %s", it);
            return new ExpressiveWord(it, knownWord);
        }).collect(Collectors.toList());
    }

}
