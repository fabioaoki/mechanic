package br.com.mechanic.mechanic.service.model;

import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
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
public class ProviderAccountModel {

    private Long id;
    private String workshop;
    private String cnpj;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private Long type;
    private ProviderAccountStatusEnum status;
}
