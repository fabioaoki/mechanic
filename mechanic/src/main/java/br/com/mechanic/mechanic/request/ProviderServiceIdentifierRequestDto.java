package br.com.mechanic.mechanic.request;

import br.com.mechanic.mechanic.enuns.ProviderServiceIdentifierEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderServiceIdentifierRequestDto {
    private List<ProviderServiceIdentifierEnum> identifierEnumList;
}
