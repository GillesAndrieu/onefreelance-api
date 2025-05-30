package org.grisbi.onefreelance.business.mappers;

import java.util.UUID;
import org.grisbi.onefreelance.model.dto.request.ClientRequest;
import org.grisbi.onefreelance.model.dto.response.ClientResponse;
import org.grisbi.onefreelance.persistence.entity.ClientEntity;
import org.grisbi.onefreelance.persistence.entity.ClientEntity.ClientDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Client mapper.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {

  ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

  /*---------------------------*/
  /* Client Entity             */
  /*---------------------------*/
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createAt", expression = "java(Instant.now())")
  @Mapping(target = "clientData", expression = "java(toClientDataEntity(clientRequest, connectedUser))")
  ClientEntity toCreateClientEntity(final ClientRequest clientRequest, final UUID connectedUser);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createAt", ignore = true)
  @Mapping(target = "clientData", expression = "java(toClientDataEntity(clientRequest, connectedUser))")
  ClientEntity toUpdateClientEntity(final UUID id, final ClientRequest clientRequest, final UUID connectedUser);

  /*---------------------------*/
  /* Client RESPONSE           */
  /*---------------------------*/

  @Mapping(target = "id", source = "clientEntity.id")
  @Mapping(target = "name", source = "clientEntity.clientData.name")
  @Mapping(target = "address", source = "clientEntity.clientData.address")
  @Mapping(target = "siret", source = "clientEntity.clientData.siret")
  @Mapping(target = "referent", source = "clientEntity.clientData.referent")
  @Mapping(target = "updateAt", source = "clientEntity.clientData.updateAt")
  ClientResponse toClientResponse(final ClientEntity clientEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "clientEntity.name")
  @Mapping(target = "address", source = "clientEntity.address")
  @Mapping(target = "siret", source = "clientEntity.siret")
  @Mapping(target = "referent", source = "clientEntity.referent")
  @Mapping(target = "updateAt", source = "clientEntity.updateAt")
  @Mapping(target = "createAt", ignore = true)
  ClientResponse toClientResponse(final ClientDataEntity clientEntity);

  /*---------------------------*/
  /* Client data Entity        */
  /*---------------------------*/

  @Mapping(target = "customerId", source = "connectedUser")
  @Mapping(target = "name", source = "clientRequest.name")
  @Mapping(target = "address", source = "clientRequest.address")
  @Mapping(target = "siret", source = "clientRequest.siret")
  @Mapping(target = "referent", source = "clientRequest.referent")
  @Mapping(target = "updateAt", ignore = true)
  ClientDataEntity toClientDataEntity(final ClientRequest clientRequest, final UUID connectedUser);
}
