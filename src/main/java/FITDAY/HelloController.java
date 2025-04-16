package FITDAY;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {
    @GetMapping
    public String Hello() {
        return "서버세팅어렵다ㅎdfsad제발ㅇㄴㄹddd";
    }
}
