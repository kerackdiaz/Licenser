package com._mas1r.licenser.config;

import com._mas1r.licenser.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
public class WebConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/v1/auth/signin", "/api/v1/auth/signup", "/api/v1/license/check/*","/api/v1/auth/recovery/*",
                                "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**","/api/v1/company/Current").permitAll()

                        .requestMatchers("/api/v1/company/Current", "/api/users/current/claims", "/api/users/current/delete-claims",
                                "/api/users/notifications-read/{id}").hasRole("USER")

                        .requestMatchers( "/api/v1/company/current", "/api/claims/update-claim", "/api/claims/delete-claim",
                                "/api/users/all", "/api/admin-super/register", "/api/admin-super/update-password", "/api/admin-super/all" ).hasRole( "ADMIN")


                        .requestMatchers( "/api/user-claims/all", "/api/user-claims/update-status", "/api/admin/create-claim",
                                "/api/claim-status-history/{id}", "/api/claim-status-history/all").hasAnyRole("ADMIN", "ADMIN_SUPER")

                        .requestMatchers("/api/claims/all").hasAnyRole("ADMIN", "ADMIN_SUPER", "USER")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManegment -> sessionManegment
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return httpSecurity.build();
    }




    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


}
