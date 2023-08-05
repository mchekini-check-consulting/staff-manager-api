package com.example.staffmanagerapi.dto.paysheet;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPaySheetDTO {

    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "Veuillez respecter le format suivant : MM/AAAA")
    private String startDate;
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "Veuillez respecter le format suivant : MM/AAAA")
    private String endDate;

}
