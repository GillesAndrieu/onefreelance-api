package org.grisbi.onefreelance.persistence.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Contract JPA repository.
 */
@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, UUID> {

  // Custom query
  @Query(value = "SELECT c.id, c.contract_data, c.create_at, cl.client_data FROM contract c "
      + "LEFT JOIN client cl on cl.id::text = c.contract_data ->> 'client_id' "
      + "WHERE c.contract_data ->> 'customer_id' = :id",
      nativeQuery = true)
  Optional<List<ContractEntity>> findAllDataById(@Param("id") String id);

  @Query(value = "SELECT c.id, c.contract_data, c.create_at, cl.client_data FROM contract c "
      + "LEFT JOIN client cl on cl.id::text = c.contract_data ->> 'client_id' "
      + "WHERE c.id = :id AND c.contract_data ->> 'customer_id' = :connectedUser",
      nativeQuery = true)
  Optional<ContractEntity> findByIdAndCustomerId(@Param("id") UUID id,
                                                 @Param("connectedUser") String connectedUser);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM contract c WHERE c.id = :id", nativeQuery = true)
  void deleteContract(@Param("id") UUID id);
}
