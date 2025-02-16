package performance.lab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/test")
    public String test(){
        return "안녕하세요 테스트입니다.";
    }
}
