package FITDAY.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecomandResponse {

    private List<String> recommendations;
    private String description;
}
