package com.sample.crm.entity;

import java.time.LocalDate;
import java.util.List;

import com.sample.crm.model.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @ManyToOne
  private Contact contact;

  @ManyToOne
  private Client client;

  @OneToMany
  private List<Comment> comments;
}
