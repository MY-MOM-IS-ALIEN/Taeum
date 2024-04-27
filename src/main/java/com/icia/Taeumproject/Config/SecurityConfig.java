package com.icia.Taeumproject.Config;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import com.icia.Taeumproject.Service.CustomUserDetailsService;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {
  
  private final CustomUserDetailsService customUserDetailsService;
  

  @Autowired
  public SecurityBeansConfig securityBeansConfig;
  
  @Autowired
  public WebSocketConfig webSocketConfig;

                        

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
      auth.userDetailsService(customUserDetailsService).passwordEncoder(securityBeansConfig.passwordEncoder());
  }
  

  
  @Bean // StaticResources  파일 보안 무시 
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
  @Bean
  public SpringSecurityDialect securityDialect(){
    return new SpringSecurityDialect();
  }
  
  @Bean      // 경로 권한 설정 
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
    http.authorizeHttpRequests((auth) ->auth
      .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
      .requestMatchers("/", "/home").hasRole("USER")
      .requestMatchers("/loginProc","/joinProc").permitAll()// "/","/login" 모든 사용자 접근
      .requestMatchers("/joinForm").permitAll()
      .requestMatchers("/ApplyForm").hasRole("USER")
      .requestMatchers("/fragments").permitAll()
      .requestMatchers("/emailCheck").permitAll()
      .requestMatchers("/ApplyProc").hasRole("USER")
      .requestMatchers("/applyWrite").permitAll()
      .requestMatchers("/handleTextMessage","/afterConnectionEstablished","/afterConnectionClosed").permitAll()
      .requestMatchers("/my-websocket-endpoint/**").permitAll() 
      .requestMatchers("/adminMain").hasRole("ADMIN")
      .requestMatchers("/adminPage").hasRole("ADMIN")
      .requestMatchers("/driverMain").permitAll()
      .requestMatchers("/onAuthenticationSuccess").permitAll()
      .requestMatchers("/response.sendRedirect()").permitAll()
      .requestMatchers("/findById").permitAll()
      .requestMatchers("/findEmail").permitAll()
      .requestMatchers("/pwdChange").permitAll()
      .requestMatchers("/authUser").permitAll()
      .requestMatchers("/createCode").permitAll()
      .requestMatchers("/createEmailForm").permitAll()
      .requestMatchers("/setContext").permitAll()
      .requestMatchers("/sendEmail").permitAll()
      .requestMatchers("/codeAuth").permitAll()
      .requestMatchers("/mailConfirm").permitAll()
      .requestMatchers("/createEmailForm").permitAll()
      .requestMatchers("/applyWrite").permitAll()
      .requestMatchers("/insertServ").permitAll()
      .requestMatchers("/mainCenter").permitAll()
      .requestMatchers("/selectNodeList").permitAll()
      .requestMatchers("/selectRideList").permitAll()
      .requestMatchers("/selectLocaldate").permitAll()
      .requestMatchers("/selectedLocal").permitAll()
      .requestMatchers("/nodeSelection").permitAll()
      
      
      
      
      .anyRequest().authenticated()
      );
      
    
    http.formLogin((auth)-> auth.loginPage("/loginForm").permitAll()
        .loginProcessingUrl("/loginProc").permitAll()
        .successHandler(new CustomSuccessHandler()).permitAll()
        .failureUrl("/loginForm").permitAll()
        );
    http.logout(
        (logout) -> logout.logoutUrl("/logout")
        .logoutSuccessUrl("/").permitAll()
        );
//    http.rememberMe(
//        (remember) -> remember.alwaysRemember(true)
//        );
    // csrf 토큰 off
    http.csrf((auth)-> auth.disable());
    
    
    return http.build();
  }
}