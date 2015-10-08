package org.lskk.lumen.speech.synthesis;

import com.google.common.base.Splitter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ceefour on 10/8/15.
 */
@Service
public class SpeechProsody {

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

    protected String createPho(String sentence) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    protected int insertPho(String sentence) {
        // TODO: implement
        throw new UnsupportedOperationException();
    }

    public Pho perform(String sentence, EmotionProsody emotionProsody) {
        final String result = createPho(sentence);
        int totalId = insertPho(result);
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
        // TODO: implement
        throw new UnsupportedOperationException();
    }

}
