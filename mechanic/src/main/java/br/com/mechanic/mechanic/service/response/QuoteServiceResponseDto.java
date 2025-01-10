package br.com.mechanic.mechanic.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QuoteServiceResponseDto {

    private Long id;
    private String providerAccountName;
    private String plate;
    private String vehicleDescription;
    private List<QuoteServiceDescriptionResponseDto> descriptions;
    private String clientName;
    private Float TotalAmount;
    private LocalDate expired;
    private LocalDate createDate;
}