package FITDAY.weather.exception;

import FITDAY.global.Code;
import FITDAY.global.GlobalException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeminiException extends GlobalException {

    public GeminiException(Code errorCode) {
        super(errorCode);
    }

    public GeminiException(Code errorCode, String message) {
        super(errorCode, message);
    }
}
