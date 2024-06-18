package br.com.mechanic.mechanic.request;

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
public class CompletedServiceValueRequest {
    private Long employeeAccountId;
    private Long providerServiceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal Amount;
}