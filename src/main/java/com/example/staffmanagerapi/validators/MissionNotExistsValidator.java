package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.MissionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MissionNotExistsValidator implements ConstraintValidator<MissionNotExists, String> {

    private final MissionRepository missionRepository;

    public MissionNotExistsValidator(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Override
    public boolean isValid(String missionName, ConstraintValidatorContext constraintValidatorContext) {
        return missionRepository.findMissionByNameMission(missionName).isEmpty();
    }
}
