package FITDAY.weather.service.impl;

import FITDAY.global.Code;
import FITDAY.global.WeatherCategory;
import FITDAY.weather.dto.common.GetXY;
import FITDAY.weather.dto.request.WeatherInfoRequest;
import FITDAY.weather.dto.response.*;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api-scheme}")
    private String apiScheme;

    @Value("${weather.api-host}")
    private String apiHost;

    @Value("${weather.api-key}")
    private String apiKey;

    @Value("${gpt.api-scheme}")
    private String gptScheme;

    @Value("${gpt.api-host}")
    private String gptHost;

    @Value("${gpt.api-key}")
    private String gptKey;

    @Value("${gpt.prompt}")
    private String gptPrompt;

    @Override
    public ResponseEntity<WeatherInfoResponse> getWeatherData(WeatherInfoRequest request) {

        WeatherInfoResponse response = new WeatherInfoResponse();

        List<WeatherHourData> weatherData = setWeatherData(request);
        response.setData(weatherData);
        response.setDes(setDesForGPT(weatherData));

        return ResponseEntity.ok(response);
    }

    public Map<String, RecomandResponse> setDesForGPT(List<WeatherHourData> data) {

        StringBuilder sb = new StringBuilder();
        sb.append(gptPrompt);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String dataJson = objectMapper.writeValueAsString(Collections.singletonMap("data", data));
            sb.append(dataJson);
        } catch (Exception e) {
            log.error("Error converting data to JSON", e);
        }

        URI url = UriComponentsBuilder.newInstance()
                .scheme(gptScheme)
                .host(gptHost)
                .queryParam("key", gptKey)
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

        return jsonParsingForGPTText(geminiRes);
    }

    public Map<String, RecomandResponse> jsonParsingForGPTText(GeminiApiResponse geminiRes) {
        Map<String, RecomandResponse> jsonMap = null;
        try {
            // "```json\n"과 "\n```"을 제거하여 JSON 형식 문자열만 추출
            String jsonString = geminiRes.getCandidates().get(0).getContent().getParts().get(0).getText().replace("```json\n", "").replace("\n```", "");

            ObjectMapper objectMapper = new ObjectMapper();
            // TypeReference로 String이 key, RecomandResponse가 value인 것을 명시적으로 나타낼 수 있음
            jsonMap = objectMapper.readValue(jsonString, new TypeReference<>() {});

        } catch (Exception e) {
            log.error("GPT JSON 파싱 에러");
        }
        return jsonMap;
    }

    public List<WeatherHourData> setWeatherData(WeatherInfoRequest request) {

        GetXY getXY = getXYByLocation(request.getPos());

        // build true로 restTemplate 인코딩x 처리 => restTemplate은 자동 인코딩 기능때문에 2중으로 인코딩되어 공공 API에서 에러가 남.
        URI url = UriComponentsBuilder.newInstance()
                .scheme(apiScheme)
                .host(apiHost)
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

        return  setDataPerHour(apiResponse);
    }

    public List<WeatherHourData> setDataPerHour(WeatherApiResponse apiResponse) {

        String resultCode = apiResponse.getResponse().getHeader().getResultCode();
        if (!"00".equals(resultCode)) {
            throw new WeatherInfoException(Code.NO_DATA, "최근 3일이내의 자료만 제공합니다.");
        }

        // 오전 7시, 오후 12시, 오후 9시 기준 날씨 정보 체크
        List<String> sList = Arrays.asList("0700", "1200", "2100");
        List<WeatherApiResponse.Item> itemList = apiResponse.getResponse().getBody().getItems().getItem();
        Map<String, WeatherHourData> dataMap = new HashMap<>();

        for (String time : sList) {
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
                log.info("날씨 enum 값 없음");
            }
        }
        return new ArrayList<>(dataMap.values());
    }

    public GetXY getXYByLocation(String location) {

        return switch (location) {
            case "서울특별시" -> new GetXY(60, 127);
            case "부산광역시" -> new GetXY(98, 76);
            case "대구광역시" -> new GetXY(89, 90);
            case "인천광역시" -> new GetXY(55, 124);
            case "광주광역시" -> new GetXY(58, 74);
            case "대전광역시" -> new GetXY(67, 100);
            case "울산광역시" -> new GetXY(102, 84);
            case "세종특별자치시" -> new GetXY(66, 103);
            case "경기도" -> new GetXY(60, 120);
            case "강원도" -> new GetXY(73, 134);
            case "충청북도" -> new GetXY(69, 107);
            case "충청남도" -> new GetXY(68, 100);
            case "전라북도" -> new GetXY(63, 89);
            case "전라남도" -> new GetXY(51, 67);
            case "경상북도" -> new GetXY(89, 91);
            case "경상남도" -> new GetXY(91, 77);
            case "제주특별자치도" -> new GetXY(52, 38);
            default -> new GetXY(60, 127); // 서울로 디폴트 처리
        };
    }
}
