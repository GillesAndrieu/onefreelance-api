package org.grisbi.onefreelance.business.mappers;

import java.util.UUID;
import org.grisbi.onefreelance.model.dto.Calculated;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.model.dto.response.ReportResponse;
import org.grisbi.onefreelance.persistence.entity.ReportEntity;
import org.grisbi.onefreelance.persistence.entity.ReportEntity.ReportDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Report mapper.
 */
@Mapper(componentModel = "spring", imports = {ClientMapper.class, ContractMapper.class})
public interface ReportMapper {

  /*---------------------------*/
  /* Client Entity             */
  /*---------------------------*/

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "contractData", ignore = true)
  @Mapping(target = "clientData", ignore = true)
  @Mapping(target = "createAt", expression = "java(Instant.now())")
  @Mapping(target = "reportData", expression = "java(toReportDataEntity(reportRequest, calculated, connectedUser))")
  ReportEntity toCreateReportEntity(final ReportRequest reportRequest, final Calculated calculated,
                                    final UUID connectedUser);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "createAt", ignore = true)
  @Mapping(target = "contractData", ignore = true)
  @Mapping(target = "clientData", ignore = true)
  @Mapping(target = "reportData", expression = "java(toReportDataEntity(reportRequest, calculated, connectedUser))")
  ReportEntity toUpdateReportEntity(final UUID id, final ReportRequest reportRequest, final Calculated calculated,
                                    final UUID connectedUser);

  /*---------------------------*/
  /* Report RESPONSE           */
  /*---------------------------*/

  @Mapping(target = "id", source = "reportEntity.id")
  @Mapping(target = "client", ignore = true)
  @Mapping(target = "contract", ignore = true)
  @Mapping(target = "contractId", source = "reportEntity.reportData.contractId")
  @Mapping(target = "month", source = "reportEntity.reportData.month")
  @Mapping(target = "year", source = "reportEntity.reportData.year")
  @Mapping(target = "billedMonth", source = "reportEntity.reportData.billedMonth")
  @Mapping(target = "billedYear", source = "reportEntity.reportData.billedYear")
  @Mapping(target = "billed", source = "reportEntity.reportData.billed")
  @Mapping(target = "activity", source = "reportEntity.reportData.activity")
  @Mapping(target = "bonus", source = "reportEntity.reportData.bonus")
  @Mapping(target = "calculated", source = "reportEntity.reportData.calculated")
  @Mapping(target = "updateAt", source = "reportEntity.reportData.updateAt")
  ReportResponse toReportResponse(final ReportEntity reportEntity);

  @Mapping(target = "id", source = "reportEntity.id")
  @Mapping(target = "contractId", ignore = true)
  @Mapping(target = "contract",
      expression = "java(ContractMapper.INSTANCE.toContractResponse(reportEntity.getContractData()))")
  @Mapping(target = "client", expression = "java(ClientMapper.INSTANCE.toClientResponse(reportEntity.getClientData()))")
  @Mapping(target = "month", source = "reportEntity.reportData.month")
  @Mapping(target = "year", source = "reportEntity.reportData.year")
  @Mapping(target = "billedMonth", source = "reportEntity.reportData.billedMonth")
  @Mapping(target = "billedYear", source = "reportEntity.reportData.billedYear")
  @Mapping(target = "billed", source = "reportEntity.reportData.billed")
  @Mapping(target = "activity", source = "reportEntity.reportData.activity")
  @Mapping(target = "bonus", source = "reportEntity.reportData.bonus")
  @Mapping(target = "calculated", source = "reportEntity.reportData.calculated")
  @Mapping(target = "updateAt", source = "reportEntity.reportData.updateAt")
  ReportResponse toReportJoinAllResponse(final ReportEntity reportEntity);

  /*---------------------------*/
  /* Client data Entity        */
  /*---------------------------*/

  @Mapping(target = "customerId", source = "connectedUser")
  @Mapping(target = "contractId", source = "reportRequest.contractId")
  @Mapping(target = "month", source = "reportRequest.month")
  @Mapping(target = "year", source = "reportRequest.year")
  @Mapping(target = "billedMonth", source = "reportRequest.billedMonth")
  @Mapping(target = "billedYear", source = "reportRequest.billedYear")
  @Mapping(target = "billed", source = "reportRequest.billed")
  @Mapping(target = "bonus", source = "reportRequest.bonus")
  @Mapping(target = "activity", source = "reportRequest.activity")
  @Mapping(target = "calculated", source = "calculated")
  @Mapping(target = "updateAt", ignore = true)
  ReportDataEntity toReportDataEntity(final ReportRequest reportRequest, final Calculated calculated,
                                      final UUID connectedUser);
}
