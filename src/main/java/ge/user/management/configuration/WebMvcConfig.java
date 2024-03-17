package ge.user.management.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Autowired
  InactiveAccountInterceptor inactiveAccountInterceptor;
  @Value("${security.publicResources}")
  private String[] publicResources;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(inactiveAccountInterceptor).excludePathPatterns(publicResources);
  }
}