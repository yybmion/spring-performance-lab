package performance.lab3.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import performance.lab3.service.CaffeineDemoService;
import performance.lab3.service.EhcacheDemoService;

@RestController
@RequestMapping("/cache-demo")
@RequiredArgsConstructor
@Slf4j
public class CacheDemoController {

    private final CaffeineDemoService caffeineDemoService;
    private final EhcacheDemoService ehcacheDemoService;

    @GetMapping("/caffeine")
    public ResponseEntity<Map<String, Object>> caffeineCacheDemo(
            @RequestParam(name = "cacheSize", defaultValue = "5") int cacheSize) {

        log.info("Caffeine 캐시 교체 데모 시작: 캐시 크기={}", cacheSize);
        Map<String, Object> result = caffeineDemoService.demonstrateReplacement(cacheSize);
        log.info("Caffeine 캐시 교체 데모 완료");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/ehcache")
    public ResponseEntity<Map<String, Object>> ehcacheDemo(
            @RequestParam(name = "cacheSize", defaultValue = "5") int cacheSize) {

        log.info("EhCache 교체 데모 시작: 캐시 크기={}", cacheSize);
        Map<String, Object> result = ehcacheDemoService.demonstrateReplacement(cacheSize);
        log.info("EhCache 교체 데모 완료");

        return ResponseEntity.ok(result);
    }
}
