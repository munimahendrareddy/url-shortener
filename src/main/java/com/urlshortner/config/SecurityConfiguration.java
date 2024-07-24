package com.urlshortner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.urlshortner.service.UserDetailsServiceImpl;

@Configuration
public class SecurityConfiguration {

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/shorten").hasAnyAuthority("USER", "CREATOR", "EDITOR", "ADMIN")
	                .requestMatchers("/{shortenString}").hasAnyAuthority("ADMIN", "CREATOR")
	               // .requestMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
	              //  .requestMatchers("/delete/**").hasAuthority("ADMIN")
	                .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .permitAll()
	        )
	        .exceptionHandling(eh -> eh
	            .accessDeniedPage("/403")
	        )
	        .httpBasic() // Use Basic Auth for APIs
	        .and()
	        .csrf().disable(); // Disable CSRF for APIs

	    return http.build();
	}
}
