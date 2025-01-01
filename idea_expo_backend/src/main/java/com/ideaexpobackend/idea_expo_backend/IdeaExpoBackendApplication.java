package com.ideaexpobackend.idea_expo_backend;

import com.ideaexpobackend.idea_expo_backend.models.Role;
import com.ideaexpobackend.idea_expo_backend.repositories.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class IdeaExpoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeaExpoBackendApplication.class, args);
	}

	@Bean
	public ApplicationRunner initializer(RoleRepository roleRepository) {
		Role r1 = new Role("ADMIN", "Administrator account having all privileges.");
		Role r2 = new Role("USER", "Default role assigned to newly created user. ");
		return args -> roleRepository.saveAll(Arrays.asList(r1,r2));
	}
}
