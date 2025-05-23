package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import br.com.mechanic.mechanic.service.model.CompletedServiceModel;
import br.com.mechanic.mechanic.service.model.EmployeeAccountModel;
import br.com.mechanic.mechanic.service.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.service.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CompletedServiceMapper {

    CompletedServiceMapper MAPPER = Mappers.getMapper(CompletedServiceMapper.class);

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


    EmployeeAccountModel toModel(EmployeeAccount entity);

    CompletedServiceModel toModel(CompletedServiceRequest dto);

    @Named("modelToEntity")
    default CompletedService modelToEntity(CompletedServiceModel model, ProviderServiceResponseDto serviceResponseDto, EmployeeAccountResponseDto employeeResponse, BigDecimal amount, long quantity, long quantityRevised) {
        return CompletedService.builder().colorId(model.getColorId()).plateId(model.getPlateId()).amount(amount.setScale(2, RoundingMode.HALF_UP)).modelId(model.getModelId()).vehicleTypeId(model.getVehicleTypeId())
                .providerAccountId(model.getProviderAccountId()).providerServiceId(serviceResponseDto.getId()).employeeAccountId(employeeResponse.getId()).quantity(quantity).quantityRevised(quantityRevised)
                .build();
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
}