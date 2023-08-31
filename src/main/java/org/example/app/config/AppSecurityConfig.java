package org.example.app.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
    Logger logerr = Logger.getLogger( AppSecurityConfig.class );

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        logerr.info( "AppSecurityConfig AuthenticationManagerBuilder1" );
        UserDetails user = User.withUsername( "root" )
                .password(passwordEncoder().encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logerr.info( "AppSecurityConfig HttpSecurity" );
        http.headers().frameOptions().disable();
        http
                .csrf().disable()
                .authorizeHttpRequests( (authz) -> authz
                                .antMatchers("/login*").permitAll()
                                .anyRequest().authenticated()
                                )
                .httpBasic( withDefaults() )
                .formLogin()
                   .loginPage("/login")
                   .loginProcessingUrl("/login/auth")
                .defaultSuccessUrl("/books/shelf", true )
                .failureUrl("/login");
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        logerr.info( "AppSecurityConfig WebSecurity" );
        return (web) -> web.ignoring().antMatchers("/images/**" );
    }

}
