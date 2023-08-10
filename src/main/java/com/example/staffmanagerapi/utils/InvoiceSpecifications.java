package com.example.staffmanagerapi.utils;

import com.example.staffmanagerapi.model.Invoice;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class InvoiceSpecifications {

    public static Specification<Invoice> withCollaboratorIds(List<Long> collaboratorIds) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.get("collaborator").get("id").in(collaboratorIds);
        };
    }

    public static Specification<Invoice> withCustomers(List<Long> clients) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.get("customer").get("id").in(clients);
        };
    }

    public static Specification<Invoice> withDates(List<LocalDate> dates) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.get("monthYear").as(LocalDate.class).in(dates);
        };
    };
}
