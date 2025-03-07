package performance.lab3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheHitRateService {

    // Spring이 관리하는 EhCache 캐시 매니저 주입
    @Qualifier("ehCacheCacheManager")
    private final CacheManager ehCacheManager;

    private final CacheManager caffeineCacheManager;

    /**
     * 80-20 법칙 기반 캐시 히트율 비교 테스트
     */
    public Map<String, Object> testSimpleHitRate(int totalRequests, int uniqueKeys) {
        Map<String, Object> results = new HashMap<>();

        String cacheName = "simpleHitRateTest";

        // 1. Caffeine 캐시 준비
        javax.cache.Cache<Object, Object> caffeineCache = caffeineCacheManager.getCache(cacheName);

        // 2. EhCache 준비 - Spring에서 관리하는 캐시 매니저 사용
        javax.cache.Cache<Object, Object> ehCache = ehCacheManager.getCache(cacheName, Object.class, Object.class);

        log.info("테스트 시작: 총 요청={}, 고유 키={}, 캐시 크기={}", totalRequests, uniqueKeys);

        // 3. 별도로 실행 - Caffeine 먼저 테스트
        int caffeineHits = 0;
        int caffeineMisses = 0;

        // Caffeine 테스트용 키 목록 생성 (동일한 키 패턴 사용)
        List<String> testKeys = new ArrayList<>();
        for (int i = 0; i < totalRequests; i++) {
            // 80%의 요청은 20%의 키에 집중됨
            String key;
            if (Math.random() < 0.8) {
                // 20%의 "인기있는" 키 중에서 랜덤 선택
                int keyIndex = (int) (Math.random() * uniqueKeys * 0.2);
                key = "popular-key-" + keyIndex;
            } else {
                // 나머지 80%의 키 중에서 랜덤 선택
                int keyIndex = (int) (Math.random() * uniqueKeys * 0.8) + (int) (uniqueKeys * 0.2);
                key = "unpopular-key-" + keyIndex;
            }
            testKeys.add(key);
        }

        // Caffeine 테스트 실행
        log.info("Caffeine 테스트 시작");
        for (String key : testKeys) {
            Object caffeineValue = caffeineCache.get(key);
            if (caffeineValue == null) {
                caffeineMisses++;
                caffeineCache.put(key, "value-" + key);
            } else {
                caffeineHits++;
            }
        }
        log.info("Caffeine 테스트 완료: 히트={}, 미스={}", caffeineHits, caffeineMisses);

        // 4. EhCache 테스트 별도 실행
        int ehcacheHits = 0;
        int ehcacheMisses = 0;

        log.info("EhCache 테스트 시작");
        for (String key : testKeys) {
            Object ehcacheValue = ehCache.get(key);
            if (ehcacheValue == null) {
                ehcacheMisses++;
                ehCache.put(key, "value-" + key);
            } else {
                ehcacheHits++;
            }
        }
        log.info("EhCache 테스트 완료: 히트={}, 미스={}", ehcacheHits, ehcacheMisses);

        // 5. 결과 수집
        double caffeineHitRate = (double) caffeineHits / totalRequests * 100;
        double ehcacheHitRate = (double) ehcacheHits / totalRequests * 100;

        results.put("설정", Map.of(
                "총_요청_수", totalRequests,
                "고유_키_수", uniqueKeys,
                "테스트_방식", "별도 실행"
        ));

        results.put("Caffeine", Map.of(
                "히트_수", caffeineHits,
                "미스_수", caffeineMisses,
                "히트율", caffeineHitRate + "%"
        ));

        results.put("EhCache", Map.of(
                "히트_수", ehcacheHits,
                "미스_수", ehcacheMisses,
                "히트율", ehcacheHitRate + "%"
        ));

        results.put("비교_요약", Map.of(
                "히트율_차이", Math.abs(caffeineHitRate - ehcacheHitRate) + "%",
                "우수_캐시", caffeineHitRate > ehcacheHitRate ? "Caffeine" : "EhCache"
        ));

        return results;
    }

    /**
     * 패턴 전환 후 캐시 적응성 테스트
     */
    public Map<String, Object> testCacheAdaptation(int totalRequests, int uniqueKeys) {
        Map<String, Object> results = new HashMap<>();

        String cacheName = "adaptionTest";

        // 1. Caffeine 캐시 준비
        javax.cache.Cache<Object, Object> caffeineCache = caffeineCacheManager.getCache(cacheName);

        // 2. EhCache 준비 - Spring에서 관리하는 캐시 매니저 사용
        javax.cache.Cache<Object, Object> ehCache = ehCacheManager.getCache(cacheName, Object.class, Object.class);

        log.info("적응성 테스트 시작: 총 요청={}, 고유 키={}", totalRequests, uniqueKeys);

        // 키 생성을 위한 두 패턴의 키 목록 준비
        List<String> firstPatternKeys = new ArrayList<>();
        List<String> secondPatternKeys = new ArrayList<>();

        // 첫 번째 패턴: 키 0-49
        for (int i = 0; i < totalRequests / 2; i++) {
            int keyIndex = (int) (Math.random() * 50);
            firstPatternKeys.add("pattern1-key-" + keyIndex);
        }

        // 두 번째 패턴: 키 50-99
        for (int i = 0; i < totalRequests / 2; i++) {
            int keyIndex = 50 + (int) (Math.random() * 50);
            secondPatternKeys.add("pattern2-key-" + keyIndex);
        }

        // 3. Caffeine 테스트 - 첫 번째 패턴
        log.info("Caffeine - 첫 번째 패턴 시작");
        for (String key : firstPatternKeys) {
            Object value = caffeineCache.get(key);
            if (value == null) {
                caffeineCache.put(key, "value-" + key);
            }
        }
        log.info("Caffeine - 첫 번째 패턴 완료");

        // 4. Caffeine 테스트 - 두 번째 패턴
        int caffeineSecondPatternHits = 0;
        int caffeineSecondPatternMisses = 0;

        log.info("Caffeine - 두 번째 패턴 시작");
        for (String key : secondPatternKeys) {
            Object caffeineValue = caffeineCache.get(key);
            if (caffeineValue == null) {
                caffeineSecondPatternMisses++;
                caffeineCache.put(key, "value-" + key);
            } else {
                caffeineSecondPatternHits++;
            }
        }
        log.info("Caffeine - 두 번째 패턴 완료: 히트={}, 미스={}",
                caffeineSecondPatternHits, caffeineSecondPatternMisses);

        // 5. EhCache 테스트 - 첫 번째 패턴
        // 캐시 초기화 후 시작
        ehCache.clear();

        log.info("EhCache - 첫 번째 패턴 시작");
        for (String key : firstPatternKeys) {
            Object value = ehCache.get(key);
            if (value == null) {
                ehCache.put(key, "value-" + key);
            }
        }
        log.info("EhCache - 첫 번째 패턴 완료");

        // 6. EhCache 테스트 - 두 번째 패턴
        int ehcacheSecondPatternHits = 0;
        int ehcacheSecondPatternMisses = 0;

        log.info("EhCache - 두 번째 패턴 시작");
        for (String key : secondPatternKeys) {
            Object ehcacheValue = ehCache.get(key);
            if (ehcacheValue == null) {
                ehcacheSecondPatternMisses++;
                ehCache.put(key, "value-" + key);
            } else {
                ehcacheSecondPatternHits++;
            }
        }
        log.info("EhCache - 두 번째 패턴 완료: 히트={}, 미스={}",
                ehcacheSecondPatternHits, ehcacheSecondPatternMisses);

        // 7. 두 번째 패턴 히트율 계산
        double caffeineSecondPatternHitRate =
                (double) caffeineSecondPatternHits / (caffeineSecondPatternHits + caffeineSecondPatternMisses) * 100;
        double ehcacheSecondPatternHitRate =
                (double) ehcacheSecondPatternHits / (ehcacheSecondPatternHits + ehcacheSecondPatternMisses) * 100;

        // 8. 결과 수집
        results.put("설정", Map.of(
                "총_요청_수", totalRequests,
                "고유_키_수", uniqueKeys,
                "패턴_전환", "키 0-49 → 키 50-99",
                "테스트_방식", "별도 실행"
        ));

        results.put("Caffeine_두번째_패턴", Map.of(
                "히트_수", caffeineSecondPatternHits,
                "미스_수", caffeineSecondPatternMisses,
                "히트율", caffeineSecondPatternHitRate + "%"
        ));

        results.put("EhCache_두번째_패턴", Map.of(
                "히트_수", ehcacheSecondPatternHits,
                "미스_수", ehcacheSecondPatternMisses,
                "히트율", ehcacheSecondPatternHitRate + "%"
        ));

        results.put("적응성_비교", Map.of(
                "히트율_차이", Math.abs(caffeineSecondPatternHitRate - ehcacheSecondPatternHitRate) + "%",
                "우수_캐시", caffeineSecondPatternHitRate > ehcacheSecondPatternHitRate ? "Caffeine" : "EhCache",
                "해석", "높은 히트율은 새로운 패턴에 더 잘 적응했음을 의미합니다"
        ));

        return results;
    }
}
