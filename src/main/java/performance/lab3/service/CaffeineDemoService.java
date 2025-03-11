package performance.lab3.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CaffeineDemoService {

    public Map<String, Object> demonstrateReplacement(int cacheSize) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> steps = new ArrayList<>();

        // Caffeine 캐시 생성 시 동기적 제거 설정
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(cacheSize)
                .executor(Runnable::run) // 동기적 실행
                .build();

        // 초기 상태 기록
        steps.add(Map.of(
                "단계", "초기 상태",
                "내용", "빈 캐시"
        ));

        // 캐시 채우기
        for (int i = 1; i <= cacheSize; i++) {
            String key = "key-" + i;
            String value = "value-" + i;

            cache.put(key, value);
            // put 후 명시적으로 cleanup 호출
            cache.cleanUp();

            // 현재 상태 기록
            Map<String, String> currentCache = new HashMap<>(cache.asMap());
            steps.add(Map.of(
                    "단계", "항목 추가 #" + i,
                    "추가된 키", key,
                    "캐시 내용", currentCache
            ));

            // 캐시 크기 확인
            log.info("항목 추가 #{}: 현재 캐시 크기={}", i, currentCache.size());
        }

        // 홀수 키 접근 패턴
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= cacheSize; j += 2) {
                String key = "key-" + j;
                cache.getIfPresent(key);
            }
        }

        // 새 항목 추가 (교체 발생)
        for (int i = cacheSize + 1; i <= cacheSize + 3; i++) {
            String key = "key-" + i;
            String value = "value-" + i;

            // 추가 전 상태 저장
            Map<String, String> beforeCache = new HashMap<>(cache.asMap());

            // 새 항목 추가
            cache.put(key, value);
            // 명시적으로 cleanup 호출
            cache.cleanUp();

            // 추가 후 상태 저장
            Map<String, String> afterCache = new HashMap<>(cache.asMap());

            // 제거된 키 찾기
            Set<String> removedKeys = new HashSet<>(beforeCache.keySet());
            removedKeys.removeAll(afterCache.keySet());

            log.info("캐시 교체 발생: 추가={}, 제거={}, 현재 크기={}",
                    key, removedKeys, afterCache.size());

            // 상태 기록
            Map<String, Object> step = new HashMap<>();
            step.put("단계", "교체 발생 #" + (i - cacheSize));
            step.put("추가된 키", key);
            step.put("제거된 키", removedKeys);
            step.put("캐시 내용", afterCache);
            steps.add(step);
        }

        result.put("캐시_유형", "Caffeine");
        result.put("캐시_크기", cacheSize);
        result.put("교체_과정", steps);
        result.put("최종_캐시_크기", cache.asMap().size());

        return result;
    }
}
