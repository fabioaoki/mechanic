package br.com.mechanic.mechanic.request;

import br.com.mechanic.mechanic.response.ClientAccountResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientPersonRequest {

    private String name;
    private LocalDate birthDate;
    private LocalDateTime createDate;
}