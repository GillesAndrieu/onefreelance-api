package org.grisbi.onefreelance.business.mappers;

import java.util.UUID;
import org.grisbi.onefreelance.model.dto.request.ContractRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.grisbi.onefreelance.persistence.entity.ContractEntity.ContractDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Contract mapper.
 */
@Mapper(componentModel = "spring", imports = {ClientMapper.class})
public interface ContractMapper {

  ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

  /*---------------------------*/
  /* Contract Entity           */
  /*---------------------------*/
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createAt", expression = "java(Instant.now())")
  @Mapping(target = "clientData", ignore = true)
  @Mapping(target = "contractData", expression = "java(toContractDataEntity(contractRequest, connectedId))")
  ContractEntity toCreateContractEntity(final ContractRequest contractRequest, final UUID connectedId);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createAt", ignore = true)
  @Mapping(target = "clientData", ignore = true)
  @Mapping(target = "contractData", expression = "java(toContractDataEntity(contractRequest, connectedId))")
  ContractEntity toUpdateContractEntity(final UUID id, final ContractRequest contractRequest, final UUID connectedId);

  /*---------------------------*/
  /* Contract RESPONSE         */
  /*---------------------------*/
  @Mapping(target = "id", source = "contractEntity.id")
  @Mapping(target = "clientId", source = "contractEntity.contractData.clientId")
  @Mapping(target = "client", ignore = true)
  @Mapping(target = "name", source = "contractEntity.contractData.name")
  @Mapping(target = "number", source = "contractEntity.contractData.number")
  @Mapping(target = "dailyRate", source = "contractEntity.contractData.dailyRate")
  @Mapping(target = "currencyDailyRate", source = "contractEntity.contractData.currencyDailyRate")
  @Mapping(target = "taxRate", source = "contractEntity.contractData.taxRate")
  @Mapping(target = "taxRateType", source = "contractEntity.contractData.taxRateType")
  @Mapping(target = "updateAt", source = "contractEntity.contractData.updateAt")
  ContractResponse toContractResponse(final ContractEntity contractEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "clientId", source = "contractEntity.clientId")
  @Mapping(target = "client", ignore = true)
  @Mapping(target = "name", source = "contractEntity.name")
  @Mapping(target = "number", source = "contractEntity.number")
  @Mapping(target = "dailyRate", source = "contractEntity.dailyRate")
  @Mapping(target = "currencyDailyRate", source = "contractEntity.currencyDailyRate")
  @Mapping(target = "taxRate", source = "contractEntity.taxRate")
  @Mapping(target = "taxRateType", source = "contractEntity.taxRateType")
  @Mapping(target = "updateAt", source = "contractEntity.updateAt")
  @Mapping(target = "createAt", ignore = true)
  ContractResponse toContractResponse(final ContractDataEntity contractEntity);

  @Mapping(target = "id", source = "contractEntity.id")
  @Mapping(target = "clientId", source = "contractEntity.contractData.clientId")
  @Mapping(target = "client",
      expression = "java(ClientMapper.INSTANCE.toClientResponse(contractEntity.getClientData()))")
  @Mapping(target = "name", source = "contractEntity.contractData.name")
  @Mapping(target = "number", source = "contractEntity.contractData.number")
  @Mapping(target = "dailyRate", source = "contractEntity.contractData.dailyRate")
  @Mapping(target = "currencyDailyRate", source = "contractEntity.contractData.currencyDailyRate")
  @Mapping(target = "taxRate", source = "contractEntity.contractData.taxRate")
  @Mapping(target = "taxRateType", source = "contractEntity.contractData.taxRateType")
  @Mapping(target = "updateAt", source = "contractEntity.contractData.updateAt")
  ContractResponse toContractJoinClientResponse(final ContractEntity contractEntity);

  /*---------------------------*/
  /* Contract data Entity      */
  /*---------------------------*/
  @Mapping(target = "customerId", source = "connectedId")
  @Mapping(target = "clientId", source = "contractRequest.clientId")
  @Mapping(target = "name", source = "contractRequest.name")
  @Mapping(target = "number", source = "contractRequest.number")
  @Mapping(target = "dailyRate", source = "contractRequest.dailyRate")
  @Mapping(target = "currencyDailyRate", source = "contractRequest.currencyDailyRate")
  @Mapping(target = "taxRate", source = "contractRequest.taxRate")
  @Mapping(target = "taxRateType", source = "contractRequest.taxRateType")
  @Mapping(target = "updateAt", ignore = true)
  ContractDataEntity toContractDataEntity(final ContractRequest contractRequest, final UUID connectedId);
}
