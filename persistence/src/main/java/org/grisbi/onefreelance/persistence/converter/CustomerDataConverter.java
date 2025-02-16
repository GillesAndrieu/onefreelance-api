package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity.CustomerDataEntity;

/**
 * The customer data converter JSONB.
 */
public class CustomerDataConverter extends JsonConverter<CustomerDataEntity> {

  CustomerDataConverter(ObjectMapper objectMapper) {
    super(objectMapper, CustomerDataEntity.class);
  }
}
