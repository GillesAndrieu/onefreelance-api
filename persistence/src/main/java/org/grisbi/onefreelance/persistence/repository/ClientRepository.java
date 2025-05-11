package org.grisbi.onefreelance.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Client JPA repository.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

  @Query(value = "SELECT c.id, c.client_data, c.create_at FROM client c "
      + "WHERE client_data ->> 'customer_id' = :id",
      nativeQuery = true)
  Optional<List<ClientEntity>> findAllByClientDataAndId(@Param("id") String id);

  @Query(value = "SELECT c.id, c.client_data, c.create_at FROM client c "
      + "WHERE id = :id AND client_data ->> 'customer_id' = :connectedUser",
      nativeQuery = true)
  Optional<ClientEntity> findByIdAndCustomerId(@Param("id")UUID id, @Param("connectedUser") String connectedUser);
}
