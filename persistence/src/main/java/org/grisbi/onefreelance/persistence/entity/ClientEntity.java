package org.grisbi.onefreelance.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grisbi.onefreelance.model.dto.Address;
import org.grisbi.onefreelance.persistence.converter.ClientDataConverter;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Client entity.
 */
@Entity(name = "client")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(updatable = false)
  private Instant createAt;

  @Convert(converter = ClientDataConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ClientDataEntity clientData;

  /**
   * Data entity.
   */
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ClientDataEntity implements Serializable {

    private UUID customerId;
    private String name;
    private Address address;
    private String siret;
    private String referent;
    @Builder.Default
    private Instant updateAt = Instant.now();
  }
}
