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
public class ClientAddressResponseDto {

    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientAccountResponseDto clientAccount;
    private String city;
    private String street;
    private String zipCode;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
    private Double latitude;
    private Double longitude;
}
