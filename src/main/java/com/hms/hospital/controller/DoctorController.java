package com.hms.hospital.controller;
import com.hms.hospital.entity.Appointment;
import com.hms.hospital.entity.User;
import com.hms.hospital.repository.AppointmentRepository;
import com.hms.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DoctorController {

    private final UserRepository userRepo;
    private final AppointmentRepository appointmentRepo;

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(Model model, Principal principal) {

        User doctor = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        LocalDate today = LocalDate.now();
        List<Appointment> todayAppointments = appointmentRepo
                .findByDoctorIdAndStartTimeBetweenOrderByStartTimeAsc(
                        doctor.getId(),
                        today.atStartOfDay(),
                        today.plusDays(1).atStartOfDay());

        List<Appointment> upcoming = appointmentRepo
                .findByDoctorIdAndStartTimeAfterOrderByStartTimeAsc(doctor.getId(), LocalDateTime.now());
        List<Appointment> doctorAppointments = appointmentRepo
                .findByDoctorIdOrderByStartTimeAsc(doctor.getId());

        model.addAttribute("doctor", doctor);
        model.addAttribute("todayAppointments", todayAppointments);
        model.addAttribute("totalToday", todayAppointments.size());
        model.addAttribute("upcoming", upcoming);
        model.addAttribute("doctorAppointments", doctorAppointments);

        return "doctor/dashboard";
    }
}
