package org.lskk.lumen.speech.synthesis;

import java.io.Serializable;

/**
 * Created by ceefour on 10/8/15.
 */
public class Pho implements Serializable {

    private int phonemeCount;

    public String toPho() {
        // TODO: implement
        String s = "";
        for (int i = 0; i < phonemeCount; i++) {
            String sampe;
            String duration;
            String pitch;
            //s += (sampa + '\t' + duration + '\t' + pitch).trim() + '\n';
        }
        throw new UnsupportedOperationException();
    }
}
