package org.grisbi.onefreelance.business.mappers;

import java.util.UUID;
import org.grisbi.onefreelance.model.dto.request.CustomerRequest;
import org.grisbi.onefreelance.model.dto.response.CustomerResponse;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity.CustomerDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Customer mapper.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

  /*---------------------------*/
  /* Customer Entity           */
  /*---------------------------*/
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createAt", expression = "java(Instant.now())")
  @Mapping(target = "customerData", source = "customerRequest")
  CustomerEntity toCreateCustomerEntity(final CustomerRequest customerRequest);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createAt", ignore = true)
  @Mapping(target = "customerData", source = "customerRequest")
  CustomerEntity toUpdateCustomerEntity(final UUID id, final CustomerRequest customerRequest);

  /*---------------------------*/
  /* Customer RESPONSE         */
  /*---------------------------*/
  @Mapping(target = "id", source = "customerEntity.id")
  @Mapping(target = "firstname", source = "customerEntity.customerData.firstname")
  @Mapping(target = "lastname", source = "customerEntity.customerData.lastname")
  @Mapping(target = "email", source = "customerEntity.customerData.email")
  @Mapping(target = "roles", source = "customerEntity.customerData.roles")
  @Mapping(target = "active", source = "customerEntity.customerData.active")
  @Mapping(target = "updateAt", source = "customerEntity.customerData.updateAt")
  CustomerResponse toCustomerResponse(final CustomerEntity customerEntity);

  /*---------------------------*/
  /* Customer data Entity      */
  /*---------------------------*/
  @Mapping(target = "firstname", source = "customerRequest.firstname")
  @Mapping(target = "lastname", source = "customerRequest.lastname")
  @Mapping(target = "email", source = "customerRequest.email")
  @Mapping(target = "roles", source = "customerRequest.roles")
  @Mapping(target = "active", source = "customerRequest.active")
  @Mapping(target = "updateAt", ignore = true)
  CustomerDataEntity toCustomerDataEntity(final CustomerRequest customerRequest);
}
