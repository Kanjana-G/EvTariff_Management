package com.example.evtariff_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.evtariff_api.model.User;
import com.example.evtariff_api.repo.UserRepository;
import com.example.evtariff_api.service.Auth_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class EvtariffApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvtariffApiApplication.class, args);
	}
	@Bean
CommandLineRunner createDemoUsers(
        UserRepository userRepository,
        Auth_service authService) {

    return args -> {

        createUserIfMissing(
                userRepository,
                authService,
                "admin",
                "admin123",
                "ADMIN"
        );

        createUserIfMissing(
                userRepository,
                authService,
                "accountant",
                "account123",
                "ACCOUNTANT"
        );

        createUserIfMissing(
                userRepository,
                authService,
                "driver",
                "driver123",
                "DRIVER"
        );
    };
}

private void createUserIfMissing(
        UserRepository userRepository,
        Auth_service authService,
        String username,
        String password,
        String role) {

    if (!userRepository.existsByUsername(username)) {

        String salt =
                authService.generateSalt();

        String hashedPassword =
                authService.hashPassword(
                        password,
                        salt
                );

        User user = new User(
                username,
                hashedPassword,
                salt,
                role
        );

        userRepository.save(user);

        System.out.println(
                "Demo user created: " + username
        );
    }
}

}
