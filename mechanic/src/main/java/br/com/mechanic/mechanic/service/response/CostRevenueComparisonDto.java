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
public class CostRevenueComparisonDto {
    private String serviceType;
    private BigDecimal totalLaborCost;
    private BigDecimal totalEquipmentCost;
    private BigDecimal totalRevenue;
}
