package br.com.mechanic.mechanic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientAccountResponseDto {

    private Long id;
    private String cpf;
    private String rg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientPersonResponseDto person;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientAddressResponseDto address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientPhoneResponseDto phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PlateResponseDto> cars;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MarcResponseDto> marcs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ColorResponseDto> colors;
    private LocalDate birthDate;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}