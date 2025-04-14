package FITDAY.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum Code {

    INTERNAL_ERROR("INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."),
    NO_DATA("NO_DATA", "데이터가 존재하지 않습니다."),
    INVALID_INPUT("INVALID_INPUT", "잘못된 요청입니다."),
    UNAUTHORIZED("UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN("FORBIDDEN", "접근 권한이 없습니다.");

    private final String code;
    private final String message;

    Code(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
