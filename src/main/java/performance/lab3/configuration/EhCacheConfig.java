package performance.lab3.configuration;

import java.net.URI;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class EhCacheConfig {

    @Bean(name = "ehCacheCacheManager")
    public JCacheCacheManager ehCacheCacheManager() throws Exception {
        return new JCacheCacheManager(jCacheManager());
    }

    @Bean
    public CacheManager jCacheManager() throws Exception {
        // ehcache.xml 파일 경로를 URI로 변환
        ClassPathResource configResource = new ClassPathResource("ehcache.xml");
        URI uri = configResource.getURI();

        // EhCache 제공자 획득
        CachingProvider provider = Caching.getCachingProvider(EhcacheCachingProvider.class.getName());

        // 설정 파일을 이용해 캐시 매니저 생성
        CacheManager cacheManager = provider.getCacheManager(uri, getClass().getClassLoader());

        return cacheManager;
    }
}
