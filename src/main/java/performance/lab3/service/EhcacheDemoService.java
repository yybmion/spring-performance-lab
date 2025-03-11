package performance.lab3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EhcacheDemoService {

    /**
     * EhCache 교체 과정 데모
     */
    public Map<String, Object> demonstrateReplacement(int cacheSize) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> steps = new ArrayList<>();

        // EhCache 직접 생성 - 올바른 타입으로 변경
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("demoCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class, String.class,
                                ResourcePoolsBuilder.heap(cacheSize)))
                .build(true);

        // 올바른 EhCache 타입 사용
        Cache<String, String> cache = cacheManager.getCache("demoCache", String.class, String.class);

        // 초기 상태
        steps.add(Map.of(
                "단계", "초기 상태",
                "내용", "빈 캐시"
        ));

        // 캐시 채우기
        for (int i = 1; i <= cacheSize; i++) {
            String key = "key-" + i;
            String value = "value-" + i;

            cache.put(key, value);

            // 현재 상태 기록
            Map<String, String> currentCache = getCurrentCacheContents(cache);
            steps.add(Map.of(
                    "단계", "항목 추가 #" + i,
                    "추가된 키", key,
                    "캐시 내용", currentCache
            ));
        }

        log.info("캐시 채움 완료, 접근 패턴 생성 시작");

        // 특정 키 자주 접근하기
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= cacheSize; j += 2) { // 홀수 키만 접근
                String key = "key-" + j;
                cache.get(key);
            }
        }

        log.info("홀수 키에 더 자주 접근함, 캐시 교체 시작");

        // 새 항목 추가하여 교체 발생
        for (int i = cacheSize + 1; i <= cacheSize + 3; i++) {
            String key = "key-" + i;
            String value = "value-" + i;

            // 추가 전 상태
            Map<String, String> beforeCache = getCurrentCacheContents(cache);

            // 새 항목 추가
            cache.put(key, value);

            // 추가 후 상태
            Map<String, String> afterCache = getCurrentCacheContents(cache);

            // 제거된 키 찾기
            Set<String> removedKeys = new HashSet<>(beforeCache.keySet());
            removedKeys.removeAll(afterCache.keySet());

            log.info("캐시 교체 발생: 추가={}, 제거={}", key, removedKeys);

            // 상태 기록
            Map<String, Object> step = new HashMap<>();
            step.put("단계", "교체 발생 #" + (i - cacheSize));
            step.put("추가된 키", key);
            step.put("제거된 키", removedKeys);
            step.put("캐시 내용", afterCache);
            steps.add(step);
        }

        result.put("캐시_유형", "EhCache");
        result.put("캐시_크기", cacheSize);
        result.put("교체_과정", steps);
        result.put("최종_캐시_크기", getCurrentCacheContents(cache).size());

        // 리소스 정리
        cacheManager.close();

        return result;
    }

    // EhCache의 현재 내용을 가져오는 헬퍼 메서드 - 올바른 타입으로 변경
    private Map<String, String> getCurrentCacheContents(Cache<String, String> cache) {
        Map<String, String> contents = new HashMap<>();
        cache.forEach(entry -> contents.put(entry.getKey(), entry.getValue()));
        return contents;
    }
}
