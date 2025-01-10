package br.com.mechanic.mechanic.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceCountDto {
    private String identifier;
    private String typeOfCar;
    private Long count;
    private BigDecimal workmanshipAmount;
    private BigDecimal equipmentValue;
}
