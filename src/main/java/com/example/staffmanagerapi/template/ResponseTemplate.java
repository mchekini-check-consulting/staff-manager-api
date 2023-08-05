package com.example.staffmanagerapi.template;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTemplate {

  private Object payload;
  private String error;
  private String message;
  private Map<String, String> validations;
}
