package org.grisbi.onefreelance.business.mappers;

import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.model.dto.response.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Login mapper.
 */
@Mapper(componentModel = "spring")
public interface LoginMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "roles", expression = "java(toRoles(roles))")
  LoginResponse toLoginResponse(UUID id, List<String> roles);

  default List<String> toRoles(List<String> roles) {
    return roles;
  }
}
