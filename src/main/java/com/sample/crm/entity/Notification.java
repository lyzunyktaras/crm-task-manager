package com.sample.crm.entity;

import com.sample.crm.model.NotificationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Notification type must not be null")
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "sent_at")
    @PastOrPresent(message = "Sent at datetime must be in past or present")
    private LocalDateTime sentAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE)
    private List<UserNotification> userNotifications = new ArrayList<>();
}
