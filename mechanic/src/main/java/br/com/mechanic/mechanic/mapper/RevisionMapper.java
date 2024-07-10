package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import br.com.mechanic.mechanic.model.CompletedServiceValueModel;
import br.com.mechanic.mechanic.model.RevisionModel;
import br.com.mechanic.mechanic.request.RevisionRequest;
import br.com.mechanic.mechanic.response.RevisionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface RevisionMapper {

    RevisionMapper MAPPER = Mappers.getMapper(RevisionMapper.class);

    Revision toEntity(RevisionRequest dto);

    RevisionResponse toDto(Revision save);

    RevisionModel toModel(Revision save);

    Revision modelToEntity(RevisionModel save);

    @Named("transactionToRequest")
    default RevisionRequest transactionToRequest(CompletedServiceValueModel completedServiceValueModel, Long transactionId, Long providerAccountId, Long clientAccountId, BigDecimal mileage, long revisionId){
        return RevisionRequest.builder()
                .providerServiceId(completedServiceValueModel.getProviderServiceId())
                .endDate(completedServiceValueModel.getEndDate())
                .providerAccountId(providerAccountId)
                .mileageForInspection(completedServiceValueModel.getMileageForInspection())
                .mileage(mileage)
                .quantity(completedServiceValueModel.getQuantity())
                .revisionId(revisionId)
                .completedServiceId(transactionId).clientAccountId(clientAccountId).build();
    }

}
