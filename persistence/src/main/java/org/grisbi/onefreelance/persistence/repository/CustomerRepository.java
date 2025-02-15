package org.grisbi.onefreelance.persistence.repository;

import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Customer JPA repository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

}
