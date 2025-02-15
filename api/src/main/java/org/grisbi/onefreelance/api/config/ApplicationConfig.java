package org.grisbi.onefreelance.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The application configuration.
 */
@Configuration
@EntityScan("org.grisbi.onefreelance.persistence")
@EnableJpaRepositories("org.grisbi.onefreelance.persistence")
public class ApplicationConfig {
}
