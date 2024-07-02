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
public class RevisionRequest {
    private Long completedServiceId;
    private Long providerServiceId;
    private Long providerAccountId;
    private Long clientAccountId;
    private LocalDate endDate;
    private BigDecimal mileageForInspection;
    private BigDecimal mileage;
}