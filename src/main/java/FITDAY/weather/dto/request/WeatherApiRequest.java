package FITDAY.weather.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherApiRequest {

    private String baseUrl;
    private String serviceKey;
    private String numOfRows;
    private String baseDate;
    private String baseTime;
    private String nx;
    private String ny;
    private String dataType;
}
