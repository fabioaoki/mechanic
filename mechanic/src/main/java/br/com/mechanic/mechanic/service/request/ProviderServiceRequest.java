package br.com.mechanic.mechanic.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceRequest {
    private Long providerAccountId;
    private List<Long> vehicleTypeIds;
    private List<Long> serviceIdentifierIds;
}