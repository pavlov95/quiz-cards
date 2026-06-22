package com.quizCards.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    public static final String USER_ID_FROM_SESSION = "user_id";

    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/",
            "/login",
            "/register",
            "/error"
    );

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String endpoint = request.getRequestURI()
                .substring(request.getContextPath().length());

        if (PUBLIC_ENDPOINTS.contains(endpoint)) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute(USER_ID_FROM_SESSION) == null) {

            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }

        return true;
    }
}