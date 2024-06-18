package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RevisionRequest {
    private Long providerAccountId;
    private Long employeeAccountId;
    private Long providerServiceId;
    private Long vehicleTypeId;
    private Long colorId;
    private Long plateId;
    private Long modelId;
}