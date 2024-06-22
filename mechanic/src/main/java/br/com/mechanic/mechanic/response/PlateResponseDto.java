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
public class PlateResponseDto {

    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long clientAccountId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mercosulPlate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String oldPlate;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}