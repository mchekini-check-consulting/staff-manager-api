package com.example.staffmanagerapi.dto.activity.in;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
  @Valid
  private List<ActivityDto> activities;
}
