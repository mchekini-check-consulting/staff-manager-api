package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.repository.ActivityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

  @Autowired
  private ActivityRepository activityRepository;

  public List<Activity> createActivities(List<ActivityDto> data) {
    List<Activity> records = data
      .stream()
      .map(row ->
        Activity
          .builder()
          .date(row.getDate())
          .quantity(row.getQuantity())
          .category(row.getCategory())
          .comment(row.getComment())
          .build()
      )
      .toList();

    return this.activityRepository.saveAll(records);
  }
}
