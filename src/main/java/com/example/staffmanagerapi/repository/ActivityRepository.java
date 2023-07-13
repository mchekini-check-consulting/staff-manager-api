package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {}
