package com.example.staffmanagerapi.handler;

import com.example.staffmanagerapi.template.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.example.staffmanagerapi.utils.Constants.GLOBAL_RESPONSE_IGNORE_PATHS;

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

        int responseStatus = ((ServletServerHttpResponse) response).getServletResponse().getStatus();

        if (responseStatus >= 200 && responseStatus < 300 && !GLOBAL_RESPONSE_IGNORE_PATHS.contains(requestPath) && body != null
        && !"application/octet-stream".equals(selectedContentType.toString())) {

            return ResponseTemplate.builder().payload(body).build();
        }

        return body;
    }
}
