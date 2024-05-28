package br.com.mechanic.mechanic.model;

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
public class ProviderPersonResponseModel {

    private Long id;
    private Long providerAccountId;
    private String name;
    private LocalDate birthDate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}
