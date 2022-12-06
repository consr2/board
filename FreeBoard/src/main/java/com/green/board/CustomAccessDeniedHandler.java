package com.green.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.util.UrlPathHelper;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	//권한 오류 페이지
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		//스프링 시큐리티 로그인 떄 만든 객체
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//현재 접속 url를 확인
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String originalURL = urlPathHelper.getOriginatingRequestUri(request);
		
		//로직을 짜서 상황에 따라 보내줄 주소를 설정
		response.sendRedirect("/user/auth/403");
		
	}

}
