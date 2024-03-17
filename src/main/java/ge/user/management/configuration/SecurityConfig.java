package ge.user.management.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  @Value("${security.publicResources}")
  private String[] publicResources;
  @Autowired
  private JwtAuthenticationFilter jwtAuthFilter;
  @Autowired
  private AuthenticationProvider authenticationProvider;
  @Autowired
  private CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
  @Autowired
  private CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;


  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(req ->
        req.requestMatchers(publicResources)
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(exceptionHandling ->
        exceptionHandling.authenticationEntryPoint(customBearerTokenAuthenticationEntryPoint)
          .accessDeniedHandler(customBearerTokenAccessDeniedHandler)
      );

    return http.build();
  }
}
