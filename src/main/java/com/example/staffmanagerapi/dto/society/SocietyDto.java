package com.example.staffmanagerapi.dto.society;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  private String name;

  @NotNull
  @Pattern(regexp = "^\\d{14}$", message="Must be a 14 length digit")
  private String siret;

  @NotNull
  @Pattern(regexp = "^FR\\d{11}$", message="Must start with FR followed by a 11 length digit")
  private String vat;

  @NotNull
  private String contact;

  @NotNull
  @Email
  private String email;

  @NotNull
  private String address;

  @NotNull
  @Pattern(regexp = "^\\d+$", message="Must have only digits")
  private String capital;
}
