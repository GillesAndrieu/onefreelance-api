package org.grisbi.onefreelance.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Customer JPA repository.
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {

  @Query(value = "SELECT c.id, c.profile, c.create_at FROM customer c WHERE profile ->> 'email' = :email",
      nativeQuery = true)
  Optional<CustomerEntity> findByProfileEmail(@Param("email") String email);
}
