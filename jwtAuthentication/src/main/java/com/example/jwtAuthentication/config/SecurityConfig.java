package com.example.jwtAuthentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    //for givign our own configuration for security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return  http
                .csrf(customizer-> customizer.disable())
                .authorizeHttpRequests(request->request
                        .requestMatchers("/register","/login").permitAll()
                        .anyRequest().authenticated())//for authenticating all the requests
                .httpBasic(Customizer.withDefaults()) // for sending request through post man and it will add popup if login form unavailable in browser.
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //By making session stateless we will get new session id but it won't ask for login credentials again and again

               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)// for adding Jwt Filter before UserPassword Authentication filter , UserPassword Authentication filter is available by default.
               .build();

         //http.formLogin(Customizer.withDefaults());//it will add form in our browser


       // return http.build(); // if you give only this no security will apply
    }

//    @Bean
////    public UserDetailsService userDetailsService(){
        //this is for Inmemory authentication
//            UserDetails user1= User
//                                .withDefaultPasswordEncoder()
//                    .username("kiran")
//                    .password("k@123")
//                    .roles("USER")
//                                .build();
//        UserDetails user2= User
//                .withDefaultPasswordEncoder()
//                .username("harsh")
//                .password("h@123")
//                .roles("ADMIN")
//                .build();
//            return new InMemoryUserDetailsManager(user1,user2);
    //}

    //AuthenticationProvider is responsible for providing the authentiction or checking the authentication nothing but username and password
    @Bean
    public AuthenticationProvider authenticationProvider(){

        //we are using DaoAuthenticationProvider because DaoAuthenticationProvider is implementing AuthenticationProvider.
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}

