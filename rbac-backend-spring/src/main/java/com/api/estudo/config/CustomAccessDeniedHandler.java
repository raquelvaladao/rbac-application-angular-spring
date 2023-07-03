package com.api.estudo.config;

import com.api.estudo.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    public CustomAccessDeniedHandler() {
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        ApiResponse errorMessage = ApiResponse.builder()
                .code("ROLE_NAO_AUTORIZADA")
                .message("Acesso negado para sua role")
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(mapper.writeValueAsString(errorMessage));
    }
}