package br.com.mechanic.mechanic.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QuoteServiceDescriptionModel {
    private String description;
    private Float value;
}
