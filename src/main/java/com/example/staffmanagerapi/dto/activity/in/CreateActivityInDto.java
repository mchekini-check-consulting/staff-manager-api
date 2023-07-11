package com.example.staffmanagerapi.dto.activity.in;

import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateActivityInDto {

  @NotNull
  private LocalDate date;

  @NotNull
  @Positive
  @Max(18)
  private Integer quantity;

  @NotNull
  private ActivityCategoryEnum category;

  @Nullable
  private String comment;
}
