package FITDAY.community.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityAuthException extends RuntimeException {
    public CommunityAuthException() {
        super("삭제 권한이 없습니다.");
    }
}
