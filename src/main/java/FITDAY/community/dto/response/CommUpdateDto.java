package FITDAY.community.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommUpdateDto {

    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private Long readCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
