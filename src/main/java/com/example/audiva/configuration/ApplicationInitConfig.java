package com.example.audiva.configuration;

import com.example.audiva.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
//                Set<com.example.audiva.entity.Role> roles = new HashSet<>();
//
//                roles.add(Role.builder().name("ADMIN").build());
//                User user = User.builder().username("admin").password(passwordEncoder.encode("admin")).roles(roles).build();

//                userRepository.save(user);
                log.warn("admin user has bean created with password: admin");
            }
        };
    }
}
