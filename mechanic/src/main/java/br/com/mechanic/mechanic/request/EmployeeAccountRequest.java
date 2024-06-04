package br.com.mechanic.mechanic.request;

import br.com.mechanic.mechanic.enuns.EmployeeRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeAccountRequest {
    private Long providerAccountId;
    private String name;
    private LocalDate birthDate;
    private EmployeeRoleEnum role;
}