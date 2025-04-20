package FITDAY.weather.service;

import FITDAY.weather.dto.request.WeatherInfoRequest;
import FITDAY.weather.dto.response.WeatherInfoResponse;
import org.springframework.http.ResponseEntity;


public interface WeatherService {
    ResponseEntity<WeatherInfoResponse> getWeatherData(WeatherInfoRequest weatherInfoRequest);
}