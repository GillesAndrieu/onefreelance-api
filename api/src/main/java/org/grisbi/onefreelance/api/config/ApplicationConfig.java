package org.grisbi.onefreelance.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The application configuration.
 */
@Configuration
@EntityScan
@EnableJpaRepositories
public class ApplicationConfig {
}
