package br.com.mechanic.mechanic.response;

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
public class CompletedResponseDtoDefault {

    private Long providerAccountId;
    private Long employeeAccountId;
    private Long providerServiceId;
    private Long transactionId;
    private Long vehicleTypeId;
    private Long colorId;
    private Long plateId;
    private Long modelId;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private BigDecimal amount;
}