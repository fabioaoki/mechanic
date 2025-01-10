package br.com.mechanic.mechanic.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionResponse {
    private Long id;
    private Long providerAccountId;
    private String completedServiceIds;
    private Long vehicleTypeId;
    private Long plateId;
    private Long colorId;
    private Long clientAccountId;
    private Long rewardId;
    private BigDecimal workmanshipAmount;
    private BigDecimal amount;
    private String vehicleName;
    private Long installments;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}