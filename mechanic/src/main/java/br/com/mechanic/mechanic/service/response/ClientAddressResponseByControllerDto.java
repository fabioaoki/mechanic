package br.com.mechanic.mechanic.service.response;

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
public class ClientAddressResponseByControllerDto {

    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long clientAccountId;
    private String city;
    private String street;
    private String zipCode;
    private LocalDateTime createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastUpdate;
    private Double latitude;
    private Double longitude;
}
