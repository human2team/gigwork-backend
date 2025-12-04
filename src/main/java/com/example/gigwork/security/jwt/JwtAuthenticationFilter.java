package com.example.gigwork.security.jwt;
import java.io.IOException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.gigwork.entity.User;
import com.example.gigwork.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            String path = request.getRequestURI();
            logger.info("JwtAuthenticationFilter: processing request {} {}", request.getMethod(), path);
            
            // permitAll 경로는 JWT 검증 skip
            if (path.equals("/") || 
                path.equals("/error") ||
                path.startsWith("/jobseeker") ||
                path.startsWith("/employer") ||
                path.startsWith("/login") || 
                path.startsWith("/signup") ||
                path.startsWith("/terms") ||
                path.startsWith("/privacy") ||
                path.startsWith("/register") || 
                path.startsWith("/api/auth/") || 
                path.startsWith("/public/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.endsWith(".html") ||
                path.startsWith("/static/") ||
                path.startsWith("/assets/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.endsWith(".svg") ||
                path.endsWith(".ico") ||
                path.equals("/favicon.ico")) {
                logger.info("JwtAuthenticationFilter: permitAll path, skipping JWT validation");
                filterChain.doFilter(request, response);
                return;
            }
            
            // 1. Header에서 JWT 토큰 추출
            String token = resolveToken(request);
            logger.info("JwtAuthenticationFilter: resolved token: {}", token == null ? "<none>" : (token.length() <= 8 ? token : token.substring(0,8) + "..."));
            
            // 2. 토큰 검증
            if (token != null && jwtTokenProvider.validateToken(token)) {
                logger.info("JwtAuthenticationFilter: token validated, extracting email");
                // 3. 토큰에서 이메일 추출
                String email = jwtTokenProvider.getEmailFromToken(token);
                
                // 4. 사용자 조회
                User user = userRepository.findByEmail(email).orElse(null);
                
                if (user != null) {
                    logger.info("JwtAuthenticationFilter: user found: {}", email);
                    // 5. Authentication 객체 생성
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))
                        );
                    
                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // 6. SecurityContext에 Authentication 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.info("JwtAuthenticationFilter: no user found for email {}", email);
                }
            } else {
                logger.info("JwtAuthenticationFilter: no valid token present or validation failed");
            }
        } catch (Exception e) {
            logger.error("JWT 인증 처리 중 오류 발생", e);
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Header에서 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
