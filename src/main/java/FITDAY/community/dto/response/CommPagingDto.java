package FITDAY.community.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommPagingDto {

    private Long id;
    private String title;
    private Long categoryId;
}
