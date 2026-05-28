
package com.hms.hospital.controller;

import com.hms.hospital.entity.*;
import com.hms.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userRepo.count());
        model.addAttribute("totalPatients", patientRepo.count());
        model.addAttribute("totalAppointments", appointmentRepo.count());
        model.addAttribute("totalDoctors", userRepo.countByRole(Role.DOCTOR));
        model.addAttribute("recentAppointments", appointmentRepo.findTop10ByOrderByStartTimeDesc());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user,
                             @RequestParam String password,
                             RedirectAttributes ra) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            ra.addFlashAttribute("error", "Email already exists!");
            return "redirect:/admin/users/new";
        }

        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepo.save(user);

        if (savedUser.getRole() == Role.PATIENT) {
            Patient patient = new Patient();
            patient.setName(savedUser.getName());
            patient.setEmail(savedUser.getEmail());
            patient.setPatientId("PAT-" + String.format("%04d", patientRepo.count() + 1));
            patient.setUser(savedUser);
            patientRepo.save(patient);
        }

        ra.addFlashAttribute("msg", "User created successfully!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, Principal principal, RedirectAttributes ra) {
        User user = userRepo.findById(id).orElseThrow();

        if (user.getEmail().equals(principal.getName())) {
            ra.addFlashAttribute("error", "You cannot delete your own account while signed in.");
            return "redirect:/admin/users";
        }

        if (user.getRole() == Role.DOCTOR) {
            appointmentRepo.deleteAll(appointmentRepo.findByDoctorId(user.getId()));
        }

        if (user.getRole() == Role.PATIENT) {
            appointmentRepo.deleteAll(appointmentRepo.findByPatientUserIdOrderByStartTimeAsc(user.getId()));
        }

        userRepo.deleteById(id);

        ra.addFlashAttribute("msg", "User deleted successfully!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam Role role, RedirectAttributes ra) {
        User user = userRepo.findById(id).orElseThrow();
        user.setRole(role);
        User savedUser = userRepo.save(user);

        if (role == Role.PATIENT && patientRepo.findByUserEmail(savedUser.getEmail()).isEmpty()) {
            Patient patient = new Patient();
            patient.setName(savedUser.getName());
            patient.setEmail(savedUser.getEmail());
            patient.setPatientId("PAT-" + String.format("%04d", patientRepo.count() + 1));
            patient.setUser(savedUser);
            patientRepo.save(patient);
        }

        ra.addFlashAttribute("msg", "Role changed to " + role + " successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/appointments")
    public String allAppointments(Model model) {
        model.addAttribute("appointments", appointmentRepo.findAllByOrderByStartTimeDesc());
        return "admin/appointments";
    }
    
    @PostMapping("/appointments/{id}/delete")
    public String deleteAppointment(@PathVariable Long id,
                                    RedirectAttributes ra) {

        appointmentRepo.deleteById(id);

        ra.addFlashAttribute("msg", "Appointment deleted successfully!");
        return "redirect:/admin/appointments";
    }

    @PostMapping("/appointments/{id}/complete")
    public String completeAppointment(@PathVariable Long id,
                                      RedirectAttributes ra) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow();
        appointment.setStatus("COMPLETED");
        appointmentRepo.save(appointment);

        ra.addFlashAttribute("msg", "Appointment marked as completed!");
        return "redirect:/admin/appointments";
    }

}
