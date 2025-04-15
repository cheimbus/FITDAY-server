package FITDAY.weather.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiApiResponse {

    private List<Candidate> candidates;

    @Getter @Setter
    public static class Candidate {
        private Content content;
    }

    @Getter @Setter
    public static class Content {
        private List<Part> parts;
    }

    @Getter @Setter
    public static class Part {
        private String text;
    }
}
