package br.com.mechanic.mechanic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipmentInModel {

    private Long id;
    private Long providerAccountId;
    private Long equipmentId;
    private Long quantity;
    private BigDecimal amount;
    private boolean finish;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}