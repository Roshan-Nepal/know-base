package com.roshan.know_base.infrastructure.persistence;


import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.roshan.know_base.auth.repo",
        "com.roshan.know_base.document.repo",
        "com.roshan.know_base.vector.repo",
        "com.roshan.know_base.ai.repo",
        }
)
@EntityScan(basePackages = {
        "com.roshan.know_base.auth.entity",
        "com.roshan.know_base.document.entity",
        "com.roshan.know_base.vector.entity",
        "com.roshan.know_base.ai.entity",
})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
}