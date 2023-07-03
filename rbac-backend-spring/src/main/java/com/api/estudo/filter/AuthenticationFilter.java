package com.api.estudo.filter;

import com.api.estudo.database.repositories.LogTableRepository;
import com.api.estudo.dto.ApiResponse;
import com.api.estudo.exceptions.EntityNotFoundException;
import com.api.estudo.models.RaceUser;
import com.api.estudo.services.AuthenticationService;
import com.api.estudo.services.LogService;
import com.api.estudo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;


@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final LogService logService;

    public AuthenticationFilter(UserService userService, AuthenticationService authenticationService, LogService logService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.logService = logService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromHeader(request);
            if (authenticationService.isTokenValid(token)) {
                setUserContext(token);
            }

            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException e) {
            setUnauthorizedResponse(response, e);
        }
    }

    private void setUnauthorizedResponse(HttpServletResponse response, EntityNotFoundException e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        ApiResponse errorMessage = ApiResponse.builder()
                .code("USUARIO_NAO_ENCONTRADO")
                .message(message)
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(errorMessage));
    }

    private void setUserContext(String token) {
        Integer userId = authenticationService.gatherUserIdFromToken(token);
        RaceUser user = userService.getUserById(userId);
        logService.logAccess(userId);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities())
                );
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = null;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        return token;
    }
}
