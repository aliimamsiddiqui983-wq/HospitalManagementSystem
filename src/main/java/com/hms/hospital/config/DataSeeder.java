package com.hms.hospital.config;

import com.hms.hospital.entity.Role;
import com.hms.hospital.entity.User;
import com.hms.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${app.seed.admin.email:admin@hospital.com}")
    private String adminEmail;

    @Value("${app.seed.admin.password:password}")
    private String adminPassword;

    @Value("${app.seed.doctor.email:sarah@hospital.com}")
    private String doctorEmail;

    @Value("${app.seed.doctor.password:password}")
    private String doctorPassword;

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        createUserIfMissing("Administrator", adminEmail, adminPassword, Role.ADMIN);
        createUserIfMissing("Dr. Sarah", doctorEmail, doctorPassword, Role.DOCTOR);
    }

    private void createUserIfMissing(String name, String email, String rawPassword, Role role) {
        userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            return userRepository.save(user);
        });
    }
}
