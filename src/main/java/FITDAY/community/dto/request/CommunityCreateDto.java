package FITDAY.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCreateDto {

    private Long categoryId;
    private String title;
    private String content;
    private Long regId;
}
