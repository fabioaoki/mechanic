package br.com.mechanic.mechanic.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipmentOutResponseDto {

    private Long id;
    private Long providerAccountId;
    private EquipmentResponseDto equipment;
    private Long completedServiceId;
    private Boolean reversal;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}