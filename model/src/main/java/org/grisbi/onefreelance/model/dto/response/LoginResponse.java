package org.grisbi.onefreelance.model.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The Login response DTO.
 */
@Value
@Builder
@Jacksonized
public class LoginResponse {

  UUID id;
  @Builder.Default
  List<String> roles = List.of();
}
