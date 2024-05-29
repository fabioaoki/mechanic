package br.com.mechanic.mechanic.model;

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
    private Long typeAccountId;
    private Long vehicleTypeId;
    private String name;
    private String identifier;
    private boolean isEnable;
}