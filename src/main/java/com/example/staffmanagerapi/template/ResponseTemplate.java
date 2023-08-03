package com.example.staffmanagerapi.template;

import com.example.staffmanagerapi.enums.ErrorsEnum;
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
  private ErrorsEnum error;
  private String message;
  private Map<String, String> validations;
}
