package FITDAY.common.service;

import FITDAY.weather.dto.common.GetXY;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    public GetXY getLocationInfo(String location) {

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
