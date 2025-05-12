package org.grisbi.onefreelance.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.persistence.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Client JPA repository.
 */
@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

  @Query(value = "SELECT r.id, r.report_data, r.create_at FROM report r "
      + "WHERE report_data ->> 'customer_id' = :id",
      nativeQuery = true)
  Optional<List<ReportEntity>> findAllByReportDataAndId(@Param("id") String id);

  @Query(value = "SELECT r.id, r.report_data, r.create_at FROM report r "
      + "WHERE id = :id AND report_data ->> 'customer_id' = :connectedUser",
      nativeQuery = true)
  Optional<ReportEntity> findByIdAndCustomerId(@Param("id")UUID id, @Param("connectedUser") String connectedUser);
}
