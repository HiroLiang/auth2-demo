package com.tfs.auth.aop;

import com.tfs.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final HttpServletRequest request;

    private final JwtUtil jwtUtil;

    @Pointcut("@annotation(com.tfs.auth.model.annotation.JwtValidation)")
    public void jwtPointcut() {}

    @Before("jwtPointcut()")
    public void validateJwt(JoinPoint joinPoint) {
        log.info("Jwt validation joint point : {}", joinPoint.getSignature().getName());

        String header = request.getHeader("JwtToken");
        if (header == null || !header.startsWith("Bearer "))
            throw new RuntimeException("Missing or invalid Authorization header");

        if(!jwtUtil.validateToken(header.replace("Bearer ", "")))
            throw new RuntimeException("Invalid token");

        Claims claims = jwtUtil.decodeToken(header.replace("Bearer ", "")).getPayload();

        request.setAttribute("subject", claims.getSubject());
    }
}
