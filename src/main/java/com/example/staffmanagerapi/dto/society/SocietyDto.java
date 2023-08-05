package com.example.staffmanagerapi.dto.society;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocietyDto {

  @NotBlank
  private String name;

  @NotBlank
  @Pattern(regexp = "^\\d{14}$", message = "Must be a 14 length digit")
  private String siret;

  @NotBlank
  @Pattern(
    regexp = "^FR\\d{11}$",
    message = "Must start with FR followed by a 11 length digit"
  )
  private String vat;

  @NotBlank
  private String contact;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String address;

  @NotBlank
  @Pattern(regexp = "^\\d+$", message = "Must have only digits")
  private String capital;
}
