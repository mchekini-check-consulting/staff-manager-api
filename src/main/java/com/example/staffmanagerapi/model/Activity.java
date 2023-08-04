package com.example.staffmanagerapi.model;

import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "collaborator_id")
  private Collaborator collaborator;

  @ManyToOne
  @JoinColumn(name = "mission_id")
  private Mission mission;

  private LocalDate date;
  private Integer quantity;
  @Enumerated(STRING)
  private ActivityCategoryEnum category;
  private String comment;
}
