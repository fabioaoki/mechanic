package br.com.mechanic.mechanic.response;

import br.com.mechanic.mechanic.enuns.ProviderServiceIdentifierEnum;
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
public class ProviderServiceResponseDto {

    private Long id;
    private Long providerAccountId;
    private Long typeServiceId;
    private Long vehicleTypeId;
    private String name;
    private ProviderServiceIdentifierEnum identifier;
    private boolean isEnabled;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}
