package com.sample.crm.configuration.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.sample.crm.configuration.security.jwt.AuthEntryPointJwt;
import com.sample.crm.configuration.security.jwt.AuthTokenFilter;
import com.sample.crm.service.UserPrincipalService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final UserPrincipalService userPrincipalService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final AuthTokenFilter authTokenFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userPrincipalService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    return http.cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(httpSecuritySessionManagementConfigurer ->
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((requests) ->
            requests.requestMatchers(
                    "/ws/**",
                    "/api/auth/**",
                    "/v3/**",
                    "/swagger-ui/**",
                    "/h2-console/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .httpBasic(withDefaults())
        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
