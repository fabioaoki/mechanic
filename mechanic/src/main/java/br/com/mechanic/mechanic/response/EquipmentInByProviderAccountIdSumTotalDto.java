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
public class EquipmentInByProviderAccountIdSumTotalDto {
    private String name;
    private BigDecimal amount;
    private Long equipmentId;
    private BigDecimal unitPrice;
    private Long quantity;
}
