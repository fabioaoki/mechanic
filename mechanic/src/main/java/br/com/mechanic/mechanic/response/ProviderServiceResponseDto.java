package br.com.mechanic.mechanic.response;

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
    private Long typeAccountId;
    private Long vehicleTypeId;
    private String name;
    private String identifier;
    private boolean isEnable;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
}
