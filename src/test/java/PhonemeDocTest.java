import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.lskk.lumen.speech.synthesis.PhonemeDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ceefour on 10/10/15.
 */
public class PhonemeDocTest {

    private static final Logger log = LoggerFactory.getLogger(PhonemeDocTest.class);

    @Test
    public void canParsePho() throws IOException {
        final String phoStr = IOUtils.toString(PhonemeDocTest.class.getResource("/salita.pho"));
        final PhonemeDoc phonemeDoc = PhonemeDoc.fromPho(phoStr);
        log.info("PhonemeDoc: {}", phonemeDoc);
        Assert.assertThat(phonemeDoc.getPhonemes(), Matchers.hasSize(8));
        Assert.assertThat(phonemeDoc.getPhonemes().get(1).getSampa(), Matchers.equalTo("V"));
        Assert.assertThat(phonemeDoc.getPhonemes().get(1).getDuration(), Matchers.equalTo(27));
        Assert.assertThat(phonemeDoc.getPhonemes().get(1).getPitches().size(), Matchers.equalTo(6));
    }
}
