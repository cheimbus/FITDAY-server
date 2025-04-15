package FITDAY.weather.exception;

import FITDAY.global.Code;
import FITDAY.global.GlobalException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherInfoException extends GlobalException {

    public WeatherInfoException(Code errorCode) {
        super(errorCode);
    }

    public WeatherInfoException(Code errorCode, String message) {
        super(errorCode, message);
    }
}
