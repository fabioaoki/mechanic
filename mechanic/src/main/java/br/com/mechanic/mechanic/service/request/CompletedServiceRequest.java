package br.com.mechanic.mechanic.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CompletedServiceRequest {
    private Long providerAccountId;
    private Long clientAccountId;
    private List<CompletedServiceValueRequest> serviceValueRequests;
    private Long vehicleTypeId;
    private String color;
    private Long plateId;
    private Long modelId;
    private Long installments;
    private BigDecimal mileage;
    private BigDecimal workmanshipAmount;

}