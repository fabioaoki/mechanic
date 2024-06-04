package br.com.mechanic.mechanic.response;

import br.com.mechanic.mechanic.enuns.EmployeeRoleEnum;
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
public class EmployeeAccountResponseDto {

    private Long id;
    private ProviderAccountResponseDto providerAccountId;
    private String name;
    private LocalDate birthDate;
    private String role;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
    private String cpf;
}