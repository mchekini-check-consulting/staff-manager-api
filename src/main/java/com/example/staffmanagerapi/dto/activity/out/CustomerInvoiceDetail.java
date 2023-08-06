package com.example.staffmanagerapi.dto.activity.out;

import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInvoiceDetail {

    ActivityCategoryEnum services;
    Double quantity;
    Double unitPriceExcludingVAT ;
    Double amountexcludingVAT;
}
