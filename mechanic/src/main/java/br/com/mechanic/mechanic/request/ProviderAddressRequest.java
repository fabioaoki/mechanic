package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderAddressRequest {
    private String street;
    private String number;
    private String neighborhood;
    private String zipCode;
    private String city;
    private String state;
}
