package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import br.com.mechanic.mechanic.model.CompletedServiceModel;
import br.com.mechanic.mechanic.model.EmployeeAccountModel;
import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CompletedServiceMapper {

    CompletedServiceMapper MAPPER = Mappers.getMapper(CompletedServiceMapper.class);

    EmployeeAccount toEntity(EmployeeAccountRequest dto);

    CompletedResponseDtoDefault toDtoDefault(CompletedService entity);

    EmployeeAccountModel toModel(EmployeeAccount entity);

    CompletedServiceModel toModel(CompletedServiceRequest dto);

    @Named("modelToEntity")
    default CompletedService modelToEntity(CompletedServiceModel model, ProviderServiceResponseDto serviceResponseDto, EmployeeAccountResponseDto employeeResponse, BigDecimal amount) {
        return CompletedService.builder().colorId(model.getColorId()).plateId(model.getPlateId()).amount(amount.setScale(2, RoundingMode.HALF_UP)).modelId(model.getModelId()).vehicleTypeId(model.getVehicleTypeId())
                .providerAccountId(model.getProviderAccountId()).providerServiceId(serviceResponseDto.getId()).employeeAccountId(employeeResponse.getId())
                .build();
    }

    @Named("toDto")
    default CompletedResponseDto toDto(String color, String workshop, String vehicleName, PlateResponseDto plate, String model, String marc, List<CompletedServiceValueResponse> responseList, Long installments, BigDecimal totalAmount) {
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
                .plate(plateValue).build();
    }
}