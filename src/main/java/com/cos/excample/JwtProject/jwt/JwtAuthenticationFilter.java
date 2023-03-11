package com.cos.excample.JwtProject.jwt;

import com.cos.excample.JwtProject.auth.PrincipalDetails;
import com.cos.excample.JwtProject.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

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
        try {
            //request에 담긴 user, password 확인해 보기
            //1) BufferedReader 활용
//            BufferedReader br = request.getReader();
//            String input = "";
//            while((input = br.readLine()) != null) {
//                System.out.println("input: " + input);
//                //{
//                //  "username": ...,
//                //  "password": ...
//                //}
//                //바디 내용 전체가 출력됨
//            }

            //2) ObjectMapper 이용
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("파싱한 User: " + user);

            //2. 정상인지 로그인 시도 해봄, authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출 -> loadByUsername 함수 실행
            //토큰 만들기
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // PrincipalDetailsService의 loadByUsername() 실행
                                                                                                    //authentication엔 로그인한 정보가 담기게 됨 -> 인증 절차
                                                                                                    //이 객체는 session 영역에 저장되게 됨 = 로그인이 되었다

            //3. return받은 PrincipalDetails를 세션에 담고(권한 관리를 위하여)
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨: " + principalDetails.getUser().getUsername());


//            System.out.println("InputStream: " + request.getInputStream());
            //org.apache.catalina.connector.CoyoteInputStream@5b2b178f
            //username, password 담겨 있음

            //리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 거임
            //굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음
            //단지 권한 처리 때문에 session에 넣어 줌
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("=====================================");


        //4. JWT토큰을 만들어서 응답함

    }

    //attemptAuthentication() 실행 후 인증이 정상적으로 되었으면 이 메소드가 실행됨
    //JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해줌
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨(인증 완료)");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
