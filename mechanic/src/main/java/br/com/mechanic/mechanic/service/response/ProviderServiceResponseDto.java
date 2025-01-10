package br.com.mechanic.mechanic.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceResponseDto {

    private Long id;
    private Long providerAccountId;
    private Long vehicleTypeId;
    private ProviderServiceIdentifierResponseDto service;
    private boolean isEnabled;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}
