package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
    Optional<Mission> findMissionByNameMission(String name);
}
