package FITDAY.weather.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherHourData {

    private String hour;
    private String tmp;
    private String sky;
    private String reh;
    private String wsd;
    private String pop;
}
