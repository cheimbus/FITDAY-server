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

    // 예보시간
    private String hour;
    // 기온
    private String tmp;
    // 하늘 상태
    private String sky;
    // 습도
    private String reh;
    // 풍속
    private String wsd;
    // 강수확률
    private String pop;
}
