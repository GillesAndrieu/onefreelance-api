package org.grisbi.onefreelance.persistence.repository;

import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Contract JPA repository.
 */
@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, UUID> {

}
