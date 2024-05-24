package com.ttwticket.backend.global;

import com.ttwticket.backend.domain.security.JwtTokenFilter;
import com.ttwticket.backend.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String secretKey;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .httpBasic().disable()
//                .csrf().disable()
//                .cors().and()
//                .authorizeRequests()
//                .antMatchers("v1/users/register", "v1/users/login").permitAll()
//                .antMatchers(HttpMethod.POST, "v1/**").authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(http -> http.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.and())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1/users/register", "/v1/users/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/**").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
