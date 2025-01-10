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
public class CompletedServiceValueResponse {
    private String  employeeAccount;
    private String providerService;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal mileageForInspection;
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private Long quantity;
}