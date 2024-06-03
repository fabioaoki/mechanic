package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipmentInRequest {
    private Long providerAccountId;
    private Long equipmentId;
    private Long quantity;
    private BigDecimal amount;
}