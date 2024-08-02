package br.com.mechanic.mechanic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventoryEfficiencyDto {
    private String equipmentName;
    private Long usageFrequency;
    private BigDecimal totalCost;
    private BigDecimal averageCostPerUse;
}
