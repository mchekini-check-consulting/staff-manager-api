package com.example.staffmanagerapi.aspect.authenticated;


import com.example.staffmanagerapi.model.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Around("execution(* *(..)) && @annotation(Authenticated)")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

        Set<String> roles = Arrays.stream(signature.getMethod().getAnnotation(Authenticated.class).hasAnyRoles())
                .collect(Collectors.toSet());
        boolean isAuthenticated = signature.getMethod().getAnnotation(Authenticated.class).authenticated();

        if (isAuthenticated && !user.isAuthenticated()) {
            response.sendError(401);
            // return null c'est un car d'erreur
            return null;
        } else if (!roles.isEmpty() && user.getRoles() != null && user.getRoles().stream().noneMatch(roles::contains)) {
            response.sendError(403);
            // la meme chose ici
            return null;
        } else {
            // Continue avec la méthode interceptée et retourne son résultat
            return proceedingJoinPoint.proceed();
        }
    }

}
