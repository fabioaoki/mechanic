package br.com.mechanic.mechanic.service.model;

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
    private String cpf;
    private LocalDate birthDate;
    private String role;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}
