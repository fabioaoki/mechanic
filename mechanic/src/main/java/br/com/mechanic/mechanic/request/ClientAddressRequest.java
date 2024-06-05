package br.com.mechanic.mechanic.request;

import br.com.mechanic.mechanic.enuns.StateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientAddressRequest {

    private String street;
    private String number;
    private String neighborhood;
    private String zipCode;
    private String city;
    private StateEnum state;
}
