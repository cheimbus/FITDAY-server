package FITDAY.weather.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoRequest {

    private String pos;
    private String date;
    private String time;
    private String dataType;
    private String numOfRows;
}
