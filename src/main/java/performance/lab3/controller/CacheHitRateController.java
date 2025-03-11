package performance.lab3.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import performance.lab3.service.CacheHitRateService;

import java.util.Map;

@RestController
@RequestMapping("/api/cache-test")
@RequiredArgsConstructor
@Slf4j
public class CacheHitRateController {

    private final CacheHitRateService cacheHitRateService;

    /**
     * 80-20 법칙 기반 캐시 히트율 비교 테스트
     * - 20%의 키가 80%의 접근을 받는 상황 시뮬레이션
     * - 캐시 크기는 전체 키의 50%로 제한하여 제거가 발생하도록 함
     */
    @GetMapping("/simple-hit-rate")
    public ResponseEntity<Map<String, Object>> testCacheHitRate(
            @RequestParam(name = "totalRequests", defaultValue = "300000") int totalRequests,
            @RequestParam(name = "uniqueKeys",defaultValue = "100") int uniqueKeys) {

        log.info("단순 히트율 테스트 시작: 요청={}, 키={}, 캐시 크기={}",
                totalRequests, uniqueKeys);

        Map<String, Object> results = cacheHitRateService.testSimpleHitRate(
                totalRequests, uniqueKeys);

        log.info("단순 히트율 테스트 완료");

        return ResponseEntity.ok(results);
    }

    /**
     * 패턴 전환 후 캐시 적응성 테스트
     * - 첫 번째 단계: 한 세트의 키에 집중 접근
     * - 두 번째 단계: 다른 세트의 키로 전환
     * 이를 통해 캐시가 사용 패턴 변화에 얼마나 잘 적응하는지 확인
     */
    @GetMapping("/adaptation-test")
    public ResponseEntity<Map<String, Object>> testCacheAdaptation(
            @RequestParam(name = "totalRequests", defaultValue = "300000") int totalRequests,
            @RequestParam(name = "uniqueKeys", defaultValue = "100") int uniqueKeys) {

        log.info("캐시 적응성 테스트 시작: 요청={}, 키={}, 캐시 크기={}",
                totalRequests, uniqueKeys);

        Map<String, Object> results = cacheHitRateService.testCacheAdaptation(
                totalRequests, uniqueKeys);

        log.info("캐시 적응성 테스트 완료");

        return ResponseEntity.ok(results);
    }
}
