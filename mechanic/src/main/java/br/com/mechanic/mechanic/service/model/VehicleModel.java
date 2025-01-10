package br.com.mechanic.mechanic.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VehicleModel {

    private Long id;
    private Long clientAccountId;
    private Long plateId;
    private Long modelId;
    private Long colorId;
    private boolean sold;
    private Long vehicleTypeId;
}