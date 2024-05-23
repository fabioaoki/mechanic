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
public class ProviderAccountResponseDto {

    private Long id;
    private String workshop;
    private String cnpj;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
    private Long type;
}
