package br.com.mechanic.mechanic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceCountGroupByDateDto {
    private String identifier;
    private String typeOfCar;
    private Long count;
    private BigDecimal workmanshipAmount;
    private BigDecimal equipmentValue;
    private LocalDate date;
}
