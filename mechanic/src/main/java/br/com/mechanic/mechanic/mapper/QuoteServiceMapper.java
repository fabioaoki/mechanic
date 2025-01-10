package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.entity.provider.QuoteServiceEntity;
import br.com.mechanic.mechanic.service.model.CompletedServiceModel;
import br.com.mechanic.mechanic.service.model.QuoteServiceDescriptionModel;
import br.com.mechanic.mechanic.service.model.QuoteServiceModel;
import br.com.mechanic.mechanic.service.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.service.request.QuoteServiceRequest;
import br.com.mechanic.mechanic.service.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mapper
public interface QuoteServiceMapper {

    QuoteServiceMapper MAPPER = Mappers.getMapper(QuoteServiceMapper.class);

    CompletedResponseDtoDefault toDtoDefault(CompletedService entity);

    @Named("byProviderAccountId")
    default CompletedResponseByProviderAccountDto byProviderAccountId(ProviderAccountResponseDto providerResponseDto,
                                                                      String employeeName, ProviderServiceResponseDto serviceResponseDto,
                                                                      String equipmentName,
                                                                      BigDecimal equipmentValue, BigDecimal workmanshipAmount,
                                                                      ModelResponseDto modelResponseDto, String vehicleType, LocalDate createDate, Long quantity) {
        BigDecimal quantityBigDecimal = BigDecimal.valueOf(quantity);
        BigDecimal totalAmount = equipmentValue.multiply(quantityBigDecimal).add(workmanshipAmount);

        return CompletedResponseByProviderAccountDto.builder()
                .workshop(providerResponseDto)
                .employeeName(employeeName)
                .service(serviceResponseDto.getService().getIdentifier())
                .vehicleType(vehicleType)
                .model(modelResponseDto.getModel())
                .name(modelResponseDto.getName())
                .version(modelResponseDto.getVersion())
                .year(modelResponseDto.getYear())
                .equipmentValue(equipmentValue)
                .totalAmount(totalAmount)
                .equipmentName(equipmentName)
                .workmanshipAmount(workmanshipAmount)
                .createDate(createDate)
                .build();
    }


    QuoteServiceModel toModel(QuoteServiceRequest request);

    CompletedServiceModel toModel(CompletedServiceRequest dto);

    @Named("modelToEntity")
    default QuoteServiceEntity modelToEntity(QuoteServiceModel model, Long providerAccountId, QuoteServiceDescriptionModel descriptionModel) {
        return QuoteServiceEntity.builder().vehicleDescription(model.getVehicleDescription()).description(descriptionModel.getDescription()).clientName(model.getClientName())
                .value(descriptionModel.getValue()).plate(model.getPlate()).createDate(LocalDateTime.now()).providerAccountId(providerAccountId).build();
    }

    @Named("toDto")
    default CompletedResponseDto toDto(String color, String workshop, String vehicleName, PlateResponseDto plate, String model, String marc, List<CompletedServiceValueResponse> responseList, Long installments, BigDecimal totalAmount, BigDecimal mileage) {
        String plateValue = (plate.getOldPlate() == null || plate.getOldPlate().isEmpty()) ? plate.getMercosulPlate() : plate.getOldPlate();
        return CompletedResponseDto.builder()
                .vehicleType(vehicleName)
                .color(color)
                .workshop(workshop)
                .model(model)
                .marc(marc)
                .installments(installments)
                .createDate(LocalDateTime.now())
                .serviceValue(responseList)
                .totalAmountPayable(totalAmount)
                .mileage(mileage)
                .plate(plateValue).build();
    }

    @Named("toResponseDto")
    default QuoteServiceResponseDto toResponseDto(AtomicReference<Long> quoteServiceId, String workshop, QuoteServiceModel quoteServiceModel, float totalAmount, LocalDate now, LocalDate expiredDate, List<QuoteServiceDescriptionResponseDto> descriptionResponseDtoList) {
        return QuoteServiceResponseDto.builder().id(quoteServiceId.get()).providerAccountName(workshop).descriptions(descriptionResponseDtoList).plate(quoteServiceModel.getPlate()).vehicleDescription(quoteServiceModel.getVehicleDescription())
                .clientName(quoteServiceModel.getClientName()).TotalAmount(totalAmount).createDate(now).expired(expiredDate).build();
    }

    List<QuoteServiceDescriptionResponseDto> toDescriptionDtoList(List<QuoteServiceDescriptionModel> descriptions);
}