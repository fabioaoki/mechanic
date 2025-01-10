package br.com.mechanic.mechanic.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QuoteServiceDescriptionResponseDto {

    private String description;
    private Float value;
}