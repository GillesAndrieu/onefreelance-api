package org.grisbi.onefreelance.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grisbi.onefreelance.model.dto.Calculated;
import org.grisbi.onefreelance.persistence.converter.ReportDataConverter;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Report entity.
 */
@Entity(name = "report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(updatable = false)
  private Instant createAt;

  @Convert(converter = ReportDataConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ReportDataEntity reportData;

  /**
   * Data entity.
   */
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ReportDataEntity implements Serializable {

    private UUID customerId;
    private UUID contractId;
    private Integer month;
    private Integer year;
    private Integer billedMonth;
    private Integer billedYear;
    private Boolean billed;
    private Map<String, BigDecimal> activity;
    private BigDecimal bonus;
    private Calculated calculated;
    @Builder.Default
    private Instant updateAt = Instant.now();
  }
}
