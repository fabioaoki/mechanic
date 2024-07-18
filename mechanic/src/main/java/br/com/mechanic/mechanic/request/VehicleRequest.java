package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VehicleRequest {

    private PlateRequest plate;
    private Long ModelId;
    private Long colorId;
    private Long vehicleTypeId;
}
