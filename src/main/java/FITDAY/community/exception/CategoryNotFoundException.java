package FITDAY.community.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long categoryId) {
        super("categoryId가 존재하지 않습니다. : " + categoryId);
    }
}
