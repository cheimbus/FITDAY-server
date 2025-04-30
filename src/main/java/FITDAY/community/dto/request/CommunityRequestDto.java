package FITDAY.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommunityRequestDto {

    private String title;
    private String content;
    private Long categoryId;
}
