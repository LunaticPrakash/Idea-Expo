package com.ideaexpobackend.idea_expo_backend.configurations;

import com.ideaexpobackend.idea_expo_backend.models.User;
import com.ideaexpobackend.idea_expo_backend.services.JwtService;
import com.ideaexpobackend.idea_expo_backend.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger("com.ideaexpobackend");
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService, JwtService jwtService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("doFilterInternal() execution started with request - {}", request.getRequestURI());
        final String authorizationHeader = request.getHeader("Authorization");
        String userEmail = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            logger.info("JWT Token: {}", jwtToken);
            try {
                userEmail = jwtService.extractUsernameFromToken(jwtToken);
                logger.info("Extracted user email from JWT Token: {}", userEmail);
            } catch (Exception e) {
                logger.error("Error processing JWT Token: {}", e.toString());
                resolver.resolveException(request, response, null, e);
            }

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwtToken, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } else {
            logger.warn("JWT token does not start with Bearer");
        }
        logger.info("doFilterInternal() execution completed");
        filterChain.doFilter(request, response);
    }
}
