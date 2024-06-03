package br.com.mechanic.mechanic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class EquipmentInResponseDto {

    private Long id;
    private Long providerAccountId;
    private Long equipmentId;
    private BigDecimal amount;
    private Long quantity;
    private boolean finish;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}