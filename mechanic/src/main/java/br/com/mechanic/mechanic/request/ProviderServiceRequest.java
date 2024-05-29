package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceRequest {
    private Long providerAccountId;
    private Long typeAccountId;
    private Long vehicleTypeId;
    private String name;
    private String identifier;
    private Boolean isEnable;
}