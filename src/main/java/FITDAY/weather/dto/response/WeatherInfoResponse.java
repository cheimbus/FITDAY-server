package FITDAY.weather.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoResponse {

    private List<WeatherHourData> data;
    private Map<String, RecomandResponse> des;
}
