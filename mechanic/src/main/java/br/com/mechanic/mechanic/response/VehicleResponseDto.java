package br.com.mechanic.mechanic.response;

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
public class VehicleResponseDto {

    private Long id;
    private ClientAccountResponseDto clientAccount;
    private PlateResponseDto plate;
    private MarcResponseDto marc;
    private ColorResponseDto color;
    private Boolean sold;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}
