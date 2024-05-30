package br.com.mechanic.mechanic.model;

import br.com.mechanic.mechanic.enuns.ProviderServiceIdentifierEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceModel {

    private Long id;
    private Long providerAccountId;
    private Long typeServiceId;
    private Long vehicleTypeId;
    private String name;
    private ProviderServiceIdentifierEnum identifier;
}