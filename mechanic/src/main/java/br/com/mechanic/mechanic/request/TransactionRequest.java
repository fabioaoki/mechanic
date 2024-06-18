package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionRequest {
    private Long providerAccountId;
    private Array completedServiceId;
    private Long vehicleTypeId;
    private Long plateId;
    private Long colorId;
    private Long clientAccountId;
    private Long rewardId;
    private BigDecimal workmanshipAmount;
    private BigDecimal amount;
    private String vehicleName;
}