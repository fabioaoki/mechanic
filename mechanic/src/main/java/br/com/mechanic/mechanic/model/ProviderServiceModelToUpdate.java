package br.com.mechanic.mechanic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceModelToUpdate {

    private Long id;
    private Long providerAccountId;
    private Long vehicleTypeId;
    private Long identifierId;
}