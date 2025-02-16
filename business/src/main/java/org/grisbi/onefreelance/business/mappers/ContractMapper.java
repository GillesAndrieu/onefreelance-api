package org.grisbi.onefreelance.business.mappers;

import java.util.UUID;
import org.grisbi.onefreelance.model.dto.request.ContractRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.grisbi.onefreelance.persistence.entity.ContractEntity.ContractDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Contract mapper.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper {

  /*---------------------------*/
  /* Contract Entity           */
  /*---------------------------*/
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createAt", expression = "java(Instant.now())")
  @Mapping(target = "contractData", source = "contractRequest")
  ContractEntity toCreateContractEntity(final ContractRequest contractRequest);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createAt", ignore = true)
  @Mapping(target = "contractData", source = "contractRequest")
  ContractEntity toUpdateContractEntity(final UUID id, final ContractRequest contractRequest);

  /*---------------------------*/
  /* Contract RESPONSE         */
  /*---------------------------*/
  @Mapping(target = "id", source = "contractEntity.id")
  @Mapping(target = "name", source = "contractEntity.contractData.name")
  @Mapping(target = "number", source = "contractEntity.contractData.number")
  @Mapping(target = "dailyRate", source = "contractEntity.contractData.dailyRate")
  @Mapping(target = "currencyDailyRate", source = "contractEntity.contractData.currencyDailyRate")
  @Mapping(target = "taxRate", source = "contractEntity.contractData.taxRate")
  @Mapping(target = "taxRateType", source = "contractEntity.contractData.taxRateType")
  @Mapping(target = "updateAt", source = "contractEntity.contractData.updateAt")
  ContractResponse toContractResponse(final ContractEntity contractEntity);

  /*---------------------------*/
  /* Contract data Entity      */
  /*---------------------------*/
  @Mapping(target = "customerId", ignore = true)
  @Mapping(target = "name", source = "contractRequest.name")
  @Mapping(target = "number", source = "contractRequest.number")
  @Mapping(target = "dailyRate", source = "contractRequest.dailyRate")
  @Mapping(target = "currencyDailyRate", source = "contractRequest.currencyDailyRate")
  @Mapping(target = "taxRate", source = "contractRequest.taxRate")
  @Mapping(target = "taxRateType", source = "contractRequest.taxRateType")
  @Mapping(target = "updateAt", ignore = true)
  ContractDataEntity toContractDataEntity(final ContractRequest contractRequest);
}
