package br.com.mechanic.mechanic.service.model;

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
public class EquipmentOutModel {

    private Long id;
    private Long providerAccountId;
    private Long equipmentId;
    private Long completedServiceId;
    private Boolean reversal;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}