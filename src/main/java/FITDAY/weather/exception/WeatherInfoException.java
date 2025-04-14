package FITDAY.weather.exception;

import FITDAY.global.Code;
import FITDAY.global.GlobalException;
import lombok.Getter;

@Getter
public class WeatherInfoException extends GlobalException {

    public WeatherInfoException(Code errorCode) {
        super(errorCode);
    }

    public WeatherInfoException(Code errorCode, String message) {
        super(errorCode, message);
    }
}
