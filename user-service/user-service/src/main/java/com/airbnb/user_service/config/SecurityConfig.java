package com.airbnb.user_service.config;
import com.airbnb.user_service.services.auth.CustomDefaultAuthUserService;
import com.airbnb.user_service.services.auth.CustomOIDCUserService;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.SupplierClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig{
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomOIDCUserService customOIDCUserService,
            CustomDefaultAuthUserService customDefaultAuthUserService)
    {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth->
                    auth
                            .requestMatchers(
                                    "/v1/auth/login-uri/**",
                                    "/v1/auth/refresh-token",
                                    "/v1/auth/error"
                            ).permitAll()
                            .anyRequest().authenticated()
            )
                .oauth2Login(
                        configure->
                                configure.userInfoEndpoint(
                                        userInfoEndpointConfig -> {
                                            userInfoEndpointConfig.oidcUserService(customOIDCUserService);
                                            userInfoEndpointConfig.userService(customDefaultAuthUserService);
                                        }
                                )
                                        .defaultSuccessUrl("/v1/auth/get-tokens",true)
                                        .failureUrl("/v1/auth/error")
                );

        return http.build();
    }

}
