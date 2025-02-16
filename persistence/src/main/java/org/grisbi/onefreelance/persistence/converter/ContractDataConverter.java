package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grisbi.onefreelance.persistence.entity.ContractEntity.ContractDataEntity;

/**
 * The contract data converter JSONB.
 */
public class ContractDataConverter extends JsonConverter<ContractDataEntity> {

  ContractDataConverter(ObjectMapper objectMapper) {
    super(objectMapper, ContractDataEntity.class);
  }
}
