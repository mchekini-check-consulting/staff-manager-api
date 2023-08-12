package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer>, JpaSpecificationExecutor<Mission> {
    Optional<Mission> findMissionByNameMission(String name);
    List<Mission> findAllByCollaborator(Collaborator collaborator);
    Optional<Mission> findByNameMissionAndIdIsNot(String name, Integer id);
}
