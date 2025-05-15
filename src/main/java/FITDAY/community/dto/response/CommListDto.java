package FITDAY.community.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommListDto {

    private Long id;
    private String title;
    private Long categoryId;
}
