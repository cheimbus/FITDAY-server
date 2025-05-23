package FITDAY.weather.service.impl;

import FITDAY.common.service.LocationService;
import FITDAY.global.Code;
import FITDAY.global.WeatherCategory;
import FITDAY.weather.dto.common.GetXY;
import FITDAY.weather.dto.request.WeatherInfoRequest;
import FITDAY.weather.dto.response.*;
import FITDAY.weather.exception.GeminiException;
import FITDAY.weather.exception.WeatherInfoException;
import FITDAY.weather.service.WeatherService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final LocationService locationService;
    private static final List<String> TARGET_HOURS = List.of("0700", "1200", "2100");

    @Value("${weather.api-scheme}")
    private String apiScheme;

    @Value("${weather.api-host}")
    private String apiHost;

    @Value("${weather.api-path}")
    private String apiPath;

    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${gemini.api-scheme}")
    private String geminiScheme;

    @Value("${gemini.api-host}")
    private String geminiHost;

    @Value("${gemini.api-path}")
    private String geminiPath;

    @Value("${gemini.api-key}")
    private String geminiKey;

    @Value("${gemini.prompt}")
    private String geminiPrompt;

    @Override
    public ResponseEntity<WeatherInfoResponse> getWeatherData(WeatherInfoRequest request) {

        WeatherInfoResponse response = new WeatherInfoResponse();

        List<WeatherHourData> weatherData = setWeatherData(request);
        Map<String, RecomandResponse> geminiData = setDesForGemini(weatherData);
        response.setData(weatherData);
        response.setDes(geminiData);

        return ResponseEntity.ok(response);
    }

    public Map<String, RecomandResponse> setDesForGemini(List<WeatherHourData> data) {

        StringBuilder sb = new StringBuilder();
        sb.append(geminiPrompt);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String dataJson = objectMapper.writeValueAsString(Collections.singletonMap("data", data));
            sb.append(dataJson);
        } catch (Exception e) {
            log.error("Error converting data to JSON", e);
        }

        URI url = UriComponentsBuilder.newInstance()
                .scheme(geminiScheme)
                .host(geminiHost)
                .path(geminiPath)
                .queryParam("key", geminiKey)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[] {
                Map.of("parts", new Object[] {
                        Map.of("text", sb.toString())
                })
        });

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        GeminiApiResponse geminiRes = restTemplate.postForObject(url, requestEntity, GeminiApiResponse.class);

        return jsonParsingForGeminiText(geminiRes);
    }

    public Map<String, RecomandResponse> jsonParsingForGeminiText(GeminiApiResponse geminiRes) {

        Map<String, RecomandResponse> jsonMap = null;
        try {
            // "```json\n"과 "\n```"을 제거하여 JSON 형식 문자열만 추출
            String jsonString = geminiRes.getCandidates().get(0).getContent().getParts().get(0).getText().replace("```json\n", "").replace("\n```", "");

            ObjectMapper objectMapper = new ObjectMapper();
            // TypeReference로 String이 key, RecomandResponse가 value인 것을 명시적으로 나타낼 수 있음
            jsonMap = objectMapper.readValue(jsonString, new TypeReference<>() {});

        } catch (Exception e) {
            throw new GeminiException(Code.NO_DATA, "Gemini parsing 실패 : " + e.getMessage());
        }
        return jsonMap;
    }

    public List<WeatherHourData> setWeatherData(WeatherInfoRequest request) {

        GetXY getXY = locationService.getLocationInfo(request.getPos());

        // build true로 restTemplate 인코딩x 처리 => restTemplate은 자동 인코딩 기능때문에 2중으로 인코딩되어 공공 API에서 에러가 남.
        URI url = UriComponentsBuilder.newInstance()
                .scheme(apiScheme)
                .host(apiHost)
                .path(apiPath)
                .queryParam("serviceKey", apiKey)
                .queryParam("numOfRows", request.getNumOfRows())
                .queryParam("base_date", request.getDate())
                .queryParam("base_time", request.getTime())
                .queryParam("nx", getXY.getX())
                .queryParam("ny", getXY.getY())
                .queryParam("dataType", request.getDataType())
                .build(true)
                .toUri();

        WeatherApiResponse apiResponse = restTemplate.getForObject(url, WeatherApiResponse.class);

        return setDataPerHour(apiResponse);
    }

    public List<WeatherHourData> setDataPerHour(WeatherApiResponse apiResponse) {

        String resultCode = apiResponse.getResponse().getHeader().getResultCode();
        if (!"00".equals(resultCode)) {
            throw new WeatherInfoException(Code.NO_DATA, "최근 3일이내의 자료만 제공합니다.");
        }

        // 오전 7시, 오후 12시, 오후 9시 기준 날씨 정보 체크
        List<WeatherApiResponse.Item> itemList = apiResponse.getResponse().getBody().getItems().getItem();
        Map<String, WeatherHourData> dataMap = new HashMap<>();

        for (String time : TARGET_HOURS) {
            WeatherHourData data = new WeatherHourData();
            data.setHour(time);
            dataMap.put(time, data);
        }

        for (WeatherApiResponse.Item item : itemList) {
            String time = item.getFcstTime();

            if (!dataMap.containsKey(time)) continue;

            WeatherHourData data = dataMap.get(time);

            try {
                WeatherCategory category = WeatherCategory.valueOf(item.getCategory());
                switch (category) {
                    case TMP -> data.setTmp(item.getFcstValue());
                    case SKY -> data.setSky(item.getFcstValue());
                    case REH -> data.setReh(item.getFcstValue());
                    case WSD -> data.setWsd(item.getFcstValue());
                    case POP -> data.setPop(item.getFcstValue());
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return new ArrayList<>(dataMap.values());
    }
}
