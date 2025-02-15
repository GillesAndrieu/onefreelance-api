package org.grisbi.onefreelance.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.grisbi.onefreelance.persistence.converter.ProfileConverter;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Customer entity.
 */
@Entity(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(updatable = false)
  private Instant createAt;

  @Convert(converter = ProfileConverter.class)
  @ColumnTransformer(write = "?::jsonb")
  @Column(columnDefinition = "jsonb")
  private ProfileEntity profile;

  /**
   * Profile entity.
   */
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ProfileEntity implements Serializable {

    private String firstname;
    private String lastname;
    private String email;
    @Builder.Default
    private List<String> roles = List.of();
    private Boolean active;
    @Builder.Default
    private Instant updateAt = Instant.now();
  }
}
