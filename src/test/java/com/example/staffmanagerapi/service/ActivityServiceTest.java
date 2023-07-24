package com.example.staffmanagerapi.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sumDaysByCategory() {
        // a faire
        assertTrue(true);
    }

    @Test
    void itShouldConvertHoursToDaysWith_3_DecimalPointsPrecision() {
        // GIVEN
        int case_1 = 8;
        int case_2 = 1;
        int case_3 = 4;
        int case_4 = 6;
        // WHEN
        Double result_1 = ActivityService.hoursToDays(case_1);
        Double result_2 = ActivityService.hoursToDays(case_2);
        Double result_3 = ActivityService.hoursToDays(case_3);
        Double result_4 = ActivityService.hoursToDays(case_4);
        // THEN
        assertEquals(result_1, 1d);
        assertEquals(result_2, 0.125d);
        assertEquals(result_3, 0.5d);
        assertEquals(result_4, 0.75d);
    }
}