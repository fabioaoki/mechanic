package br.com.mechanic.mechanic.model;

import br.com.mechanic.mechanic.enuns.EmployeeRoleEnum;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
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
public class EmployeeAccountModel {

    private Long id;
    private Long providerAccountId;
    private String name;
    private LocalDate birthDate;
    private EmployeeRoleEnum role;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}
