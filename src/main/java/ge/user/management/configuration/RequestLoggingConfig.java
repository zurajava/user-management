package ge.user.management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {

  @Bean
  public CommonsRequestLoggingFilter logFilter() {
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeQueryString(true);
    filter.setIncludePayload(false);
    filter.setMaxPayloadLength(10000);
    filter.setIncludeHeaders(false);
    filter.setBeforeMessagePrefix("START: ");
    filter.setAfterMessagePrefix("END: ");
    return filter;
  }

}