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
    private String collabotatorLastName;
    private String collabotatorFirstName;
    private int declaredDays;
    private int billedDays;
    private int rttRedemption;
    private int absenceDays;
    private double extraHoursInDays;
    private double onCallHoursInDays;
}
