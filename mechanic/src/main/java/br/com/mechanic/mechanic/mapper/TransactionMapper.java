package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import br.com.mechanic.mechanic.model.CompletedServiceModel;
import br.com.mechanic.mechanic.request.TransactionRequest;
import br.com.mechanic.mechanic.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper
public interface TransactionMapper {

    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    Transaction toEntity(TransactionRequest dto);

    TransactionResponse toDto(Transaction entity);

    @Named("completedRequestToTransactionRequest")
    default TransactionRequest completedRequestToTransactionRequest(CompletedServiceModel completedServiceModel, String completedServiceIds, BigDecimal equipmentValue, String vehicleName) {
        return TransactionRequest.builder()
                .clientAccountId(completedServiceModel.getClientAccountId())
                .providerAccountId(completedServiceModel.getProviderAccountId())
                .plateId(completedServiceModel.getPlateId())
                .colorId(completedServiceModel.getColorId())
                .vehicleTypeId(completedServiceModel.getVehicleTypeId())
                .amount(equipmentValue).rewardId(0L)
                .workmanshipAmount(completedServiceModel
                .getWorkmanshipAmount().setScale(2, RoundingMode.HALF_UP))
                .installments(completedServiceModel.getInstallments())
                .vehicleName(vehicleName)
                .completedServiceIds(completedServiceIds).build();
    }
}
