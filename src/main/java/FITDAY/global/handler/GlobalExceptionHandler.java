package FITDAY.global.handler;

import FITDAY.global.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException ex) {
        // 예외가 발생한 경우, ResponseEntity로 적절한 HTTP 상태 코드와 메시지 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // 예외에 맞는 상태 코드 설정
                .body(new ErrorResponse(ex.getErrorCode().getCode(), ex.getMessage()));
    }

    // ErrorResponse는 예외 메시지를 포함하는 DTO
    public static class ErrorResponse {
        private final String code;
        private final String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
