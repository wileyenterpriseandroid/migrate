package com.migrate.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class AjaxAwareAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException
    {
        if (isAjax(request)) {
            // pretty hacky
            // TODO: get from the spring security context?
            // TODO: better way to do this?
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please re-authenticate yourself");
        } else {
            super.commence(request, response, authException);
        }
    }

    public static boolean isAjax(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath.startsWith("/schema") || servletPath.startsWith("/classes");

        // TODO: can this work?
//        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
