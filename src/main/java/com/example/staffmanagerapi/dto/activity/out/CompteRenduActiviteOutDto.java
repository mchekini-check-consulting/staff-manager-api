package com.example.staffmanagerapi.dto.activity.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteRenduActiviteOutDto {
    private String collaboratorLastName;
    private String collaboratorFirstName;
    private Double declaredDays;
    private Double billedDays;
    private Double rttRedemption;
    private Double absenceDays;
    private Double extraHoursInDays;
    private Double onCallHoursInDays;
}
