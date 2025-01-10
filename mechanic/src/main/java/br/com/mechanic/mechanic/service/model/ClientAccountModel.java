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
public class ClientAccountModel {

    private Long id;
    private String cpf;
    private String name;
    private String rg;
    private LocalDate birthDate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}
