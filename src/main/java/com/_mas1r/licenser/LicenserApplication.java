package com._mas1r.licenser;

import com._mas1r.licenser.models.*;
import com._mas1r.licenser.repositories.CompanyRepository;
import com._mas1r.licenser.repositories.MasterRepository;
import com._mas1r.licenser.repositories.ProjectRepository;
import com._mas1r.licenser.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class LicenserApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenserApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(MasterRepository masterRepository, ProjectRepository projectRepository, CompanyRepository companyRepository, LicenseService licenseService) {
		return args -> {
			MasterAdmin masterAdmin = new MasterAdmin();
			masterAdmin.setFirstName("Ricardo");
			masterAdmin.setLastName("Diaz");
			masterAdmin.setEmail("SuperAdmin@licenser.3mas1r.com");
			masterAdmin.setUsername("kerack");
			masterAdmin.setPassword(passwordEncoder.encode("Test1234"));
			masterAdmin.setRole("ADMIN_SUPER");
			masterRepository.save(masterAdmin);



			Company company = new Company();
			company.setCompanyName("Test Company");
			companyRepository.save(company);

			Project project = new Project();
			project.setClientName("John Doe");
			project.setClientAddress("123 Main St");
			project.setClientPhone("555-1234");
			project.setClientDni("12345678");
			project.setProviderHost("provider.com");
			project.setProviderDomain("provider.com");
			project.setClientEmail("client@example.com");
			project.setProjectName("Test Project");
			project.setDescription("This is a test project.");
			project.setProjectUrl("testproject.com");
			project.setType(ProjectType.WEB);
			project.setInitDate(LocalDate.now());
			project.setExpDate(LocalDate.now().plusYears(1));
			project.setLicenseType(LicenseType.ANUAL);
			project.setRedirect("http://redirect.com");
			project.setPaymentUrl("http://payment.com");
			project.setPrice(1000.0);
			project.setBalance(500.0);
			project.setStatusLicense(true);
			project.setStatusProject(true);
			project.setCompany(company);

			projectRepository.save(project);
			License license = licenseService.createLicense(LicenseType.ANUAL, LocalDate.now(), project);
			project.setLicense(license);

			projectRepository.save(project);
		};
	}
}
