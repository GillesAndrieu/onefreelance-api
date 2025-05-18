package org.grisbi.onefreelance.persistence.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grisbi.onefreelance.persistence.converter.ClientDataConverter;
import org.grisbi.onefreelance.persistence.converter.ContractDataConverter;
import org.grisbi.onefreelance.persistence.entity.ClientEntity;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Contract with all data entity
 */
@Entity(name = "contractAll")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractJoinClientEntity implements Serializable {

  @Id
  private UUID id;
  private Instant createAt;

  @Convert(converter = ContractDataConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ContractEntity.ContractDataEntity contractData;

  @Convert(converter = ClientDataConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ClientEntity.ClientDataEntity clientData;
}
