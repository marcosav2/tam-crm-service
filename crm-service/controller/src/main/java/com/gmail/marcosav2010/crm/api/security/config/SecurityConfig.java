package com.gmail.marcosav2010.crm.api.security.config;

import com.gmail.marcosav2010.crm.api.security.AuthTokenFilter;
import com.gmail.marcosav2010.crm.api.security.JWTProvider;
import com.gmail.marcosav2010.crm.api.security.UserDetailsRetriever;
import com.gmail.marcosav2010.crm.user.entities.UserRole;
import com.gmail.marcosav2010.crm.user.usecases.GetUserByUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Value("${security.jwt.secret}")
  private String jwtSecret;

  @Value("${security.jwt.expiration}")
  private int jwtExpiration;

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      AuthTokenFilter authTokenFilter,
      AuthenticationProvider authenticationProvider,
      HandlerExceptionResolver handlerExceptionResolver)
      throws Exception {
    return http.authorizeHttpRequests(
            c ->
                c.requestMatchers("/v1/customers/**")
                    .hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
                    .requestMatchers("/v1/users/**")
                    .hasRole(UserRole.ADMIN.name())
                    .requestMatchers("/auth/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            e ->
                e.authenticationEntryPoint(
                        (request, response, ex) ->
                            handlerExceptionResolver.resolveException(request, response, null, ex))
                    .accessDeniedHandler(
                        (request, response, ex) ->
                            handlerExceptionResolver.resolveException(request, response, null, ex)))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public JWTProvider jwtProvider() {
    return new JWTProvider(jwtSecret, jwtExpiration);
  }

  @Bean
  public UserDetailsService userDetailsService(GetUserByUsername getUserByUsername) {
    return new UserDetailsRetriever(getUserByUsername);
  }

  @Bean
  public AuthTokenFilter authTokenFilter(
      HandlerExceptionResolver handlerExceptionResolver,
      JWTProvider jwtProvider,
      UserDetailsService userDetailsService) {
    return new AuthTokenFilter(handlerExceptionResolver, jwtProvider, userDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    final var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }
}
