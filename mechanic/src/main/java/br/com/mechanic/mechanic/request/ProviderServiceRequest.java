package br.com.mechanic.mechanic.request;

import br.com.mechanic.mechanic.enuns.ProviderServiceIdentifierEnum;
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
    private Long typeServiceId;
    private Long vehicleTypeId;
    private String name;
    private ProviderServiceIdentifierEnum identifier;
}