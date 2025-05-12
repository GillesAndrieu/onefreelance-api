package org.grisbi.onefreelance.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Contract JPA repository.
 */
@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, UUID> {

  @Query(value = "SELECT c.id, c.contract_data, c.create_at FROM contract c "
      + "WHERE contract_data ->> 'customer_id' = :id",
      nativeQuery = true)
  Optional<List<ContractEntity>> findAllByContractDataAndId(@Param("id") String id);

  @Query(value = "SELECT c.id, c.contract_data, c.create_at FROM contract c "
      + "WHERE id = :id AND contract_data ->> 'customer_id' = :connectedUser",
      nativeQuery = true)
  Optional<ContractEntity> findByIdAndCustomerId(@Param("id") UUID id, @Param("connectedUser") String connectedUser);
}
