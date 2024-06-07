package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientAccountRequest {
    private String cpf;
    private String rg;
    private ClientAddressRequest address;
    private ClientPersonRequest person;
    private ClientPhoneRequest phone;
    private List<VehicleRequest> vehicles;
}