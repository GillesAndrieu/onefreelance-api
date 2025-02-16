package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grisbi.onefreelance.persistence.entity.ClientEntity.ClientDataEntity;

/**
 * The client data converter JSONB.
 */
public class ClientDataConverter extends JsonConverter<ClientDataEntity> {

  ClientDataConverter(ObjectMapper objectMapper) {
    super(objectMapper, ClientDataEntity.class);
  }
}
