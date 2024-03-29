package com.cos.excample.JwtProject.config;

import com.cos.excample.JwtProject.filter.MyFilter1;
import com.cos.excample.JwtProject.filter.MyFilter3;
import com.cos.excample.JwtProject.jwt.JwtAuthenticationFilter;
import com.cos.excample.JwtProject.jwt.JwtAuthorizationFilter;
import com.cos.excample.JwtProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class); //여기 필터가 시큐리티가 실행되기 전에 먼저 실행됨
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용하지 않겠다
                .and()
                .addFilter(corsFilter) //모든 요청은 filter를 거쳐 cors 정책에서 벗어날 수 있음
                                        //@CrossOrigin(인증X)
                .formLogin().disable() //폼으로 안 하고 토큰으로 할 거임
                .httpBasic().disable() //기본적인 http 방식은 안 씀
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) //AuthenticationManager를 매개변수로 해야 함
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll(); //다른 요청은 모두 권한 없이 들어갈 수 있음



    }
}
