package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity.ProfileEntity;

/**
 * The Profile converter JSONB.
 */
public class ProfileConverter extends JsonConverter<ProfileEntity> {

  ProfileConverter(ObjectMapper objectMapper) {
    super(objectMapper, ProfileEntity.class);
  }
}
