package com.example.staffmanagerapi.security;

import com.example.staffmanagerapi.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.staffmanagerapi.utils.Constants.HEADER_AUTHORIZATION;
import static com.example.staffmanagerapi.utils.Constants.SPACE_SEPARATOR;

@Component
@Slf4j
public class SecurityFilter implements Filter {

    private final User user;

    @Value("${oauth2.resource-server.jwt.issuer-uri-collab}")
    String collabIssuerUri;

    @Value("${oauth2.resource-server.jwt.issuer-uri-admin}")
    String adminIssuerUri;

    @Autowired
    public SecurityFilter(User user) {
        if (user == null) this.user = User.builder().build();
        else this.user = user;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.split(SPACE_SEPARATOR).length == 2) {
            String token = request.getHeader(HEADER_AUTHORIZATION).split(SPACE_SEPARATOR)[1];

            String tokenClaims = JwtHelper.decode(token).getClaims();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claimMap = objectMapper.readValue(tokenClaims, new TypeReference<>() {
            });

            if (claimMap.get("iss").toString().endsWith("staff-manager-collab")) {
                decodeTokenAndPopulateUser(token, collabIssuerUri);

            } else if (claimMap.get("iss").toString().endsWith("staff-manager-admin")) {
                decodeTokenAndPopulateUser(token, adminIssuerUri);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void decodeTokenAndPopulateUser(String token, String issuer) {
        JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);
        try {
            Jwt jwt = jwtDecoder.decode(token);


            user.setUserName(jwt.getClaims().get("preferred_username").toString());
            user.setEmail(jwt.getClaims().get("email").toString());
            user.setAuthenticated(true);
            List<String> roles = (List<String>) ((LinkedTreeMap) jwt.getClaims().get("realm_access")).get("roles");
            roles = roles.stream()
                    .map(String::toString)
                    .collect(Collectors.toList());

            user.setRoles(roles);


        } catch (Exception e) {
            log.info("Failed to decode the token");
        }
    }
}
