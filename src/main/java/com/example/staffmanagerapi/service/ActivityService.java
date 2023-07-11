package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.activity.in.CreateActivityInDto;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

  @Autowired
  private ActivityRepository activityRepository;

  public Activity createActivity(CreateActivityInDto data) {
    Activity record = Activity
      .builder()
      .date(data.getDate())
      .quantity(data.getQuantity())
      .category(data.getCategory())
      .comment(data.getComment())
      .build();

    return this.activityRepository.save(record);
  }
}
