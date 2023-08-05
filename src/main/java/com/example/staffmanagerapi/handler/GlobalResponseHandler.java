package com.example.staffmanagerapi.handler;

import com.example.staffmanagerapi.template.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class GlobalResponseHandler implements ResponseBodyAdvice {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
    Object body,
    MethodParameter returnType,
    MediaType selectedContentType,
    Class selectedConverterType,
    ServerHttpRequest request,
    ServerHttpResponse response
  ) {
    String requestPath = request.getURI().getPath();

    if (requestPath.equals("/v3/api-docs") || requestPath.startsWith("/v3/api-docs/swagger-config")) {
      log.warn("You are calling the swagger endpoint, no need to apply ResponseTemplate !");
      return body;
    }
    if (body instanceof ResponseTemplate) {
      return body;
    } 

    if (body instanceof HashMap) {
      String error = ((HashMap<String, String>) body).get("error");

      if (error != null) {
        String message = ((HashMap<String, String>) body).get("message");
        return ResponseTemplate.builder().error(error.toUpperCase()).message(message).build();
      }

      return ResponseTemplate.builder().payload(body).build();
    } 

    return body;
  }
}
