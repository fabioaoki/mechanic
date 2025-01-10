package br.com.mechanic.mechanic.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReversalCompletedServiceRequest {
    private Long quantity;
    private Long completedServiceId;
    private Long equipmentId;
    private BigDecimal workmanshipAmount;

}