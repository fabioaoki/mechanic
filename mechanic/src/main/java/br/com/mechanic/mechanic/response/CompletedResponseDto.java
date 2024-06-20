package br.com.mechanic.mechanic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CompletedResponseDto {

    private String workshop;
    private List<CompletedServiceValueResponse> serviceValue;
    private String vehicleType;
    private String color;
    private String plate;
    private String model;
    private String marc;
    private Long installments;
    private BigDecimal mileage;
    private LocalDateTime createDate;
    private BigDecimal totalAmountPayable;
}