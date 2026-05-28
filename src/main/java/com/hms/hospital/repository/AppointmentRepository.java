package com.hms.hospital.repository;

import com.hms.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    long countByPatientIdAndStartTimeAfter(Long patientId, LocalDateTime now);
    long countByPatientIdAndStartTimeBefore(Long patientId, LocalDateTime now);
    long countByPatientIdAndStatus(Long patientId, String status);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findAllByOrderByStartTimeDesc();
    List<Appointment> findTop10ByOrderByStartTimeDesc();

    List<Appointment> findByDoctorIdAndStartTimeAfterOrderByStartTimeAsc(Long doctorId, LocalDateTime now);

    List<Appointment> findByPatientIdAndStartTimeAfter(Long patientId, LocalDateTime now);

    List<Appointment> findByDoctorIdAndStartTimeBetweenOrderByStartTimeAsc(Long doctorId,
                                                                           LocalDateTime start,
                                                                           LocalDateTime end);

    List<Appointment> findByDoctorIdOrderByStartTimeAsc(Long doctorId);
    List<Appointment> findByPatientIdOrderByStartTimeAsc(Long patientId);
	List<Appointment> findByPatientUserIdOrderByStartTimeAsc(Long userId);
}
