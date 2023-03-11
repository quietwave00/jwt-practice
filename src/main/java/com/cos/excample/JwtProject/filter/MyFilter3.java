package com.cos.excample.JwtProject.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        //POST 요청일 시 출력
        //토큰을 만들었다고 가정했을 때
        //인증이 되면 controller에 접근할 수 있게 함

        //토크을 생성해야 하는데
        //id, pwd가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 응담해줘야 함
        //요청할 때마다 headerd의 Authorization에 value값으로 토큰을 가져옴
        //그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지 검증해 주면 됨(RSA, HS256)
        if(req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth: " + headerAuth);
            System.out.println("필터3");
            if(headerAuth.equals("cos")) { //header에서 가져온 Authorization의 값이 cos일 때만 Filter 동작
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안 됨");
            }
        }
    }
}
