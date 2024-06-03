package br.com.mechanic.mechanic.model;

import br.com.mechanic.mechanic.enuns.ProviderServiceIdentifierEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceModel {

    private Long id;
    private Long providerAccountId;
    private List<Long> vehicleTypeIds;
    private List<Long> identifierIds;
}