package org.lskk.lumen.speech.expression;

/**
 * Created by ceefour on 03/10/2015.
 */
public class SpeechExpressionException extends RuntimeException {

    public SpeechExpressionException() {
    }

    public SpeechExpressionException(String message) {
        super(message);
    }

    public SpeechExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpeechExpressionException(Throwable cause) {
        super(cause);
    }

    public SpeechExpressionException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }

    public SpeechExpressionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
