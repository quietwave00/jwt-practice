package com.cos.excample.JwtProject.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//스프링 시큐리티에는 UsernamePasswordAuthenticationFilter가 있음
//login 요청해서 username, password를 전송하면(POST)
//이 필터가 동작함
//SecurityConfig에 등록 필요

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("AuthenticationManager: 로그인 시도 중");

        //1. username, password 받아서

        //2. 정상인지 로그인 시도 해봄, authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출 -> loadByUsername 함수 실행

        //3. return받은 PrincipalDetails를 세션에 담고(권한 관리를 위하여)

        //4. JWT토큰을 만들어서 응답함

        return super.attemptAuthentication(request, response);
    }
}
