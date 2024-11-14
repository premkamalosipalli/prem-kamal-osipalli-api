package com.osipalli.template.services.identity.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsService inMemoryUserDetailsManager() {
        var userBuilder = User.builder();
        UserDetails himanshu = userBuilder.username("prem").
                password("{bcrypt}$2y$10$opPGzLgS8o/sAegOa5kS/OdrYCgKX4hCKhl.2c/xlQNlugbX0xqZe").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(himanshu);
    }

}
