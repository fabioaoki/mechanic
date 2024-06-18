package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import br.com.mechanic.mechanic.model.CompletedServiceModel;
import br.com.mechanic.mechanic.request.TransactionRequest;
import br.com.mechanic.mechanic.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    Transaction toEntity(TransactionRequest dto);

    TransactionResponse toDto(Transaction entity);

    @Named("completedRequestToTransactionRequest")
    default TransactionRequest completedRequestToTransactionRequest(CompletedServiceModel completedServiceModel, Long[] completedServiceIds, BigDecimal equipmentValue, String vehicleName) {
        return TransactionRequest.builder()
                .clientAccountId(completedServiceModel.getClientAccountId())
                .providerAccountId(completedServiceModel.getProviderAccountId())
                .plateId(completedServiceModel.getPlateId())
                .colorId(completedServiceModel.getColorId())
                .vehicleTypeId(completedServiceModel.getVehicleTypeId())
                .amount(equipmentValue).rewardId(0L)
                .workmanshipAmount(completedServiceModel
                .getWorkmanshipAmount())
                .vehicleName(vehicleName)
                .completedServiceId(completedServiceIds).build();
    }
}
