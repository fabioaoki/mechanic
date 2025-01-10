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
public class QuoteServiceRequest {
    private String plate;
    private String vehicleDescription;
    private List<QuoteServiceDescriptionRequestDto> descriptions;
    private String clientName;
}