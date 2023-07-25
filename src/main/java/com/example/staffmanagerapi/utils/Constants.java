package com.example.staffmanagerapi.utils;

import java.util.ArrayList;
import com.example.staffmanagerapi.enums.ActivityCategoryEnum;

import java.util.EnumSet;
import java.util.Set;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String SPACE_SEPARATOR = " ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String DATE_REGEX="^(0[1-9]|[12][0-9]|[3][01])/(0[1-9]|1[012])/\\d{4}$";

    public static final List<String> AUTHORIZED_FILE_EXTENSION = new ArrayList<>(Arrays.asList("pdf", "jpg", "jpeg"));
    //------------------------------------------------------------------------------------------------
    // Règles de calcul des categories compte-rendu-activités
    //------------------------------------------------------------------------------------------------

    public static final Integer HOURS_TO_DAY_RATIO = 8;

    public static final Set<ActivityCategoryEnum> DECLARED_DAYS_CATEGORIES = EnumSet.of(
            ActivityCategoryEnum.JOUR_TRAVAILLE,
            ActivityCategoryEnum.INTERCONTRAT,
            ActivityCategoryEnum.RACHAT_RTT
    );
    public static final Set<ActivityCategoryEnum> ABSENCE_DAYS_CATEGORIES = EnumSet.of(
            ActivityCategoryEnum.CONGE_MATERNITE,
            ActivityCategoryEnum.CONGE_PATERNITE,
            ActivityCategoryEnum.CONGE_SANS_SOLDE,
            ActivityCategoryEnum.CONGE_PAYE,
            ActivityCategoryEnum.ARRET_MALADIE,
            ActivityCategoryEnum.RTT
    );
    public static final Set<ActivityCategoryEnum> BILLED_DAYS_CATEGORIES = EnumSet.of(
            ActivityCategoryEnum.JOUR_TRAVAILLE
    );
    public static final Set<ActivityCategoryEnum> ON_CALL_HOURS_IN_DAYS_CATEGORIES = EnumSet.of(
            ActivityCategoryEnum.ASTREINTE
    );
    public static final Set<ActivityCategoryEnum> EXTRA_HOURS_IN_DAYS_CATEGORIES = EnumSet.of(
            ActivityCategoryEnum.HEURE_SUPPLEMENTAIRE
    );
    public static final Set<ActivityCategoryEnum> RTT_REDEMPTION_DAYS_CATEGORIES = EnumSet.of(
            ActivityCategoryEnum.RACHAT_RTT
    );

    public static String BUCKET_NAME_JUSTIFICATIF = "justificatifs-check-consulting";
}
