package FITDAY.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/common")
public class CommonController {
    @GetMapping
    public String Hello() {
        return "test!";
    }

//    @GetMapping("/write")
//    public void write() throws IOException {
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/Users/siu/Desktop/workspace/FITDAY/db/mysql/csv/community_dummy.csv"));
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        for (int i = 1; i <= 3_000_000; i++) {
//            String title = "제목 " + i;
//            String content = "내용 더미 " + i;
//            long categoryId = (i % 10) + 1;
//            String line = String.join(",",
//                    title.replace(",", ""),
//                    content.replace(",", ""),
//                    String.valueOf(categoryId),
//                    "0","0","0",
//                    "1","1",
//                    LocalDateTime.now().format(fmt),
//                    LocalDateTime.now().format(fmt)
//            );
//            bufferedWriter.write(line);
//            bufferedWriter.newLine();
//            if (i % 10_000 == 0) bufferedWriter.flush();
//        }
//        bufferedWriter.close();
//        System.out.println("CSV 생성 완료");
//    }
}
