package com.example.staffmanagerapi.aspect.authenticated;


import com.example.staffmanagerapi.model.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Aspect
@Component
@Slf4j
public class AuthenticatedAspect {


    private final User user;
    private final HttpServletResponse response;

    @Autowired
    public AuthenticatedAspect(User user, HttpServletResponse response) {
        this.user = user;
        this.response = response;
    }

    @Before("execution(* *(..)) && @annotation(Authenticated)")
    public void before(JoinPoint joinPoint) throws IOException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Set<String> roles = Arrays.stream(signature.getMethod().getAnnotation(Authenticated.class).hasAnyRoles())
                .collect(Collectors.toSet());
        boolean isAuthenticated = signature.getMethod().getAnnotation(Authenticated.class).authenticated();

        if (isAuthenticated && !user.isAuthenticated()) response.sendError(401);
        else if (user.getRoles() != null && user.getRoles().stream().noneMatch(roles::contains))
            response.sendError(403);

    }
}
