package br.com.mechanic.mechanic.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QuoteServiceModel {

    private String plate;
    private String vehicleDescription;
    private List<QuoteServiceDescriptionModel> descriptions;
    private String clientName;
}
