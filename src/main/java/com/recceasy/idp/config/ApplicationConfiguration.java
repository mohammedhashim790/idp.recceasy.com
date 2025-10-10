package com.recceasy.idp.config;


import com.recceasy.idp.layers.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//// Configuration class for application-level beans
//@Configuration
//public class ApplicationConfiguration {
////    private final UserRepository userRepository;
//
////    public ApplicationConfiguration(UserRepository userRepository) {
////        this.userRepository = userRepository;
////    }
//
//    // Load user details
////    @Bean
////    TenantService userDetailsService() {
////        return username -> userRepository.findByEmail(username)
////                .orElseThrow(() -> new UsernameNotFoundException("Tenant not found"));
////    }
//
//    @Bean
//    BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
////        return config.getAuthenticationManager();
////    }
////
////    @Bean
////    AuthenticationProvider authenticationProvider() {
////        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
////
////        authProvider.setUser(userDetailsService());
////        authProvider.setPasswordEncoder(passwordEncoder());
////
////        return authProvider;
////    }
//}
