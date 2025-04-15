package FITDAY.weather.controller;

import FITDAY.weather.dto.request.WeatherInfoRequest;
import FITDAY.weather.dto.response.WeatherInfoResponse;
import FITDAY.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping(value = "getWeatherData")
    public ResponseEntity<WeatherInfoResponse> getWeatherData(WeatherInfoRequest weatherInfoRequest) {
        return weatherService.getWeatherData(weatherInfoRequest);
    }
}
