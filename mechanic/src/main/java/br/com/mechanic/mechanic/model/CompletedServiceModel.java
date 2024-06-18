package br.com.mechanic.mechanic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CompletedServiceModel {

    private Long id;
    private Long providerAccountId;
    private List<CompletedServiceValueModel> serviceValueRequests;
    private Long vehicleTypeId;
    private Long colorId;
    private Long plateId;
    private Long modelId;
}
