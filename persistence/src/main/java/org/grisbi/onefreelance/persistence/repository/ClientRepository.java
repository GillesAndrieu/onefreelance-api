package org.grisbi.onefreelance.persistence.repository;

import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Client JPA repository.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

}
