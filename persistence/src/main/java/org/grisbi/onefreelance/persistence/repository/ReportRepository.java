package org.grisbi.onefreelance.persistence.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Client JPA repository.
 */
@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

  @Query(value = "SELECT r.id, r.report_data, r.create_at, c.contract_data, cl.client_data "
      + "FROM report r "
      + "    LEFT JOIN contract c on c.id::text = r.report_data ->> 'contract_id' "
      + "    LEFT JOIN client cl on cl.id::text = c.contract_data ->> 'client_id' "
      + "WHERE r.report_data ->> 'customer_id' = :id",
      nativeQuery = true)
  Optional<List<ReportEntity>> findAllByCustomerId(@Param("id") String id);

  @Query(value = "SELECT r.id, r.report_data, r.create_at, c.contract_data, cl.client_data "
      + "FROM report r "
      + "    LEFT JOIN contract c on c.id::text = r.report_data ->> 'contract_id' "
      + "    LEFT JOIN client cl on cl.id::text = c.contract_data ->> 'client_id' "
      + "WHERE r.id = :id AND r.report_data ->> 'customer_id' = :connectedUser",
      nativeQuery = true)
  Optional<ReportEntity> findByIdAndCustomerId(@Param("id")UUID id, @Param("connectedUser") String connectedUser);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM report r WHERE r.id = :id", nativeQuery = true)
  void deleteReport(@Param("id") UUID id);
}
