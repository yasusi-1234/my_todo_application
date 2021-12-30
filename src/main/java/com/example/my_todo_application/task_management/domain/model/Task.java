package com.example.my_todo_application.task_management.domain.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String taskName;

    private String detail;

    @Column(nullable = false)
    private LocalDateTime startDatetime;
    @Column(nullable = false)
    private LocalDateTime endDatetime;

    @Enumerated(EnumType.STRING)
    private Importance importance;
    @Column(nullable = false)
    private int progress;
    @Column(nullable = false)
    private boolean notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    @ToString.Exclude
    private AppUser appUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return taskId != null && Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
