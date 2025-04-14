package FITDAY.global;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final Code errorCode;

    // 일반적인 에러 코드
    public GlobalException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 에러 메시지와 함께 특정 에러 코드 사용
    public GlobalException(Code errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
