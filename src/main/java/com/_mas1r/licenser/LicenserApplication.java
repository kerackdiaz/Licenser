package com._mas1r.licenser;

import com._mas1r.licenser.models.MasterAdmin;
import com._mas1r.licenser.repositories.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LicenserApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenserApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(MasterRepository masterRepository) {
		return args -> {
			MasterAdmin masterAdmin = new MasterAdmin();
			masterAdmin.setFirstName("Ricardo");
			masterAdmin.setLastName("Diaz");
			masterAdmin.setEmail("SuperAdmin@licenser.3mas1r.com");
			masterAdmin.setUsername("kerack");
			masterAdmin.setPassword(passwordEncoder.encode("Test1234"));
			masterAdmin.setRole("ADMIN_SUPER");
			masterRepository.save(masterAdmin);
		};
	}
}
