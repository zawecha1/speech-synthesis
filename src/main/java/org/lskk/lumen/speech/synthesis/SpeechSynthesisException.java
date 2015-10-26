package org.lskk.lumen.speech.synthesis;

/**
 * Created by ceefour on 03/10/2015.
 */
public class SpeechSynthesisException extends RuntimeException {

    public SpeechSynthesisException() {
    }

    public SpeechSynthesisException(String message) {
        super(message);
    }

    public SpeechSynthesisException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpeechSynthesisException(Throwable cause) {
        super(cause);
    }

    public SpeechSynthesisException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

    public SpeechSynthesisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
