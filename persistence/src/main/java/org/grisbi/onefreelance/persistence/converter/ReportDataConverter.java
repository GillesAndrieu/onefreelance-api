package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.grisbi.onefreelance.persistence.entity.ReportEntity.ReportDataEntity;

/**
 * The client data converter JSONB.
 */
public class ReportDataConverter extends JsonConverter<ReportDataEntity> {

  ReportDataConverter(ObjectMapper objectMapper) {
    super(objectMapper, ReportDataEntity.class);
  }
}
