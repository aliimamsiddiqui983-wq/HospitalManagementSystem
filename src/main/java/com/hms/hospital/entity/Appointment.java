package com.hms.hospital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Builder.Default
    private String status = "SCHEDULED";

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Patient patient;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User doctor;
}
