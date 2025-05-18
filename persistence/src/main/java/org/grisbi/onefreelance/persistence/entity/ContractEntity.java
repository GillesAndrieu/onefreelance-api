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
import java.util.Currency;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grisbi.onefreelance.model.dto.TaxRateType;
import org.grisbi.onefreelance.persistence.converter.ClientDataConverter;
import org.grisbi.onefreelance.persistence.converter.ContractDataConverter;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Contract entity.
 */
@Entity(name = "contract")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(updatable = false)
  private Instant createAt;

  @Convert(converter = ContractDataConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ContractDataEntity contractData;

  @Convert(converter = ClientDataConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ClientEntity.ClientDataEntity clientData;

  /**
   * Data entity.
   */
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ContractDataEntity implements Serializable {

    private UUID customerId;
    private UUID clientId;
    private String name;
    private String number;
    private BigDecimal dailyRate;
    private Currency currencyDailyRate;
    private BigDecimal taxRate;
    private TaxRateType taxRateType;
    @Builder.Default
    private Instant updateAt = Instant.now();
  }
}
