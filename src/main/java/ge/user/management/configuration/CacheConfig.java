package ge.user.management.configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
  @Value("${hazelcast.clusterAddress}")
  private String[] clusterAddress;

  @Bean
  public HazelcastInstance hazelcastInstance() {
    ClientConfig config = new ClientConfig();
    config.getNetworkConfig().addAddress(clusterAddress);
    return HazelcastClient.newHazelcastClient(config);
  }

  @Override
  public CacheManager cacheManager() {
    return new HazelcastCacheManager(hazelcastInstance());
  }
}
