package com.assessment.pm;

import com.assessment.pm.application.service.AuthService;
import com.assessment.pm.application.service.ProjectService;
import com.assessment.pm.application.service.TaskService;
import com.assessment.pm.domain.ports.out.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentApplication.class, args);
    }

    @Bean
    public ProjectService projectService(ProjectRepositoryPort projectRepository,
            TaskRepositoryPort taskRepository,
            NotificationPort notificationPort,
            AuditLogPort auditLogPort) {
        return new ProjectService(projectRepository, taskRepository, notificationPort, auditLogPort);
    }

    @Bean
    public TaskService taskService(TaskRepositoryPort taskRepository,
            ProjectRepositoryPort projectRepository,
            NotificationPort notificationPort,
            AuditLogPort auditLogPort) {
        return new TaskService(taskRepository, projectRepository, notificationPort, auditLogPort);
    }

    @Bean
    public AuthService authService(UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenProviderPort tokenProvider) {
        return new AuthService(userRepository, passwordEncoder, tokenProvider);
    }
}
