package br.com.mechanic.mechanic.service.response;

import br.com.mechanic.mechanic.enuns.ProviderServiceIdentifierEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceIdentifierResponseDto {

    private Long id;
    private String identifier;
}
