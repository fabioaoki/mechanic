package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CompletedServiceRequest {
    private Long providerAccountId;
    private List<CompletedServiceValueRequest> serviceValueRequests;
    private Long vehicleTypeId;
    private String color;
    private Long plateId;
    private Long modelId;
}