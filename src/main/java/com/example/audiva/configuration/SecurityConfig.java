package com.example.audiva.configuration;

import com.example.audiva.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userService;

    @Autowired
    CustomJwtDecoder customJwtDecoder;

    private final String[] PUBLIC_POST_MATCHERS = {"/users", "/auth/*", "/api/playlists/**", "/api/history/**"};

    private static final String[] PUBLIC_GET_MATCHERS = {
            "/audio/**", "/users", "/artists/**", "/api/songs/**",
            "/api/premium/payment-return", "/api/albums/**",
            "/api/history/**", "/api/playlists", "/api/search/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, PUBLIC_POST_MATCHERS).permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_MATCHERS).permitAll()
                                .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

        );
        httpSecurity.
                csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}