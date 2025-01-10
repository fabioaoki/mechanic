package br.com.mechanic.mechanic.service.response;

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
public class CompletedResponseByProviderAccountDto {

    private ProviderAccountResponseDto workshop;
    private String employeeName;
    private String service;
    private String vehicleType;
    private String name;
    private String model;
    private String version;
    private String year;
    private String equipmentName;
    private BigDecimal totalAmount;
    private BigDecimal equipmentValue;
    private BigDecimal workmanshipAmount;
    private LocalDate createDate;
}