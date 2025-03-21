package br.com.mechanic.mechanic.service.model;

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
public class CompletedServiceModel {

    private Long id;
    private Long providerAccountId;
    private Long clientAccountId;
    private List<CompletedServiceValueModel> serviceValueRequests;
    private Long vehicleTypeId;
    private Long colorId;
    private Long plateId;
    private Long modelId;
    private BigDecimal mileage;
    private Long installments;
    private BigDecimal workmanshipAmount;
}
