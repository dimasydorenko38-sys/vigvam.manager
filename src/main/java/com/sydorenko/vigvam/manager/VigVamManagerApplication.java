package com.sydorenko.vigvam.manager;

import ch.qos.logback.core.net.server.Client;
import com.sydorenko.vigvam.manager.persistence.entities.users.EmployeeEntity;
import com.sydorenko.vigvam.manager.service.CreateSuperAdminService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class VigVamManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VigVamManagerApplication.class, args);
	}

    @Configuration
    public class DbCheckConfig {

        @Bean
        public CommandLineRunner checkDatabaseConnection(DataSource dataSource, CreateSuperAdminService createSuperAdminService) {
            return args -> {
                try (Connection connection = dataSource.getConnection()) {
                    if (connection != null && !connection.isClosed()) {
                        System.out.println("🚀 УСПІХ: Підключення до бази vigvam_db встановлено!");
                        System.out.println("Драйвер: " + connection.getMetaData().getDriverName());

                        createSuperAdminService.createSuperAdmin();
                        System.out.println("✅ Процес перевірки/створення адміна завершено.");
                    }
                } catch (Exception e) {
                    System.err.println("❌ ПОМИЛКА: Не вдалося підключитися до бази!");
                    System.err.println("Причина: " + e.getMessage());
                }
            };
        }
    }


}
