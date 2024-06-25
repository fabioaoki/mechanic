package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SaveVehicleRequest {

    private Long plateId;
    private Long clientAccountId;
    private Long modelId;
    private Long colorId;
    private Long vehicleTypeId;
}
