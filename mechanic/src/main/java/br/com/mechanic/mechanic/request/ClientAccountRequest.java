package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientAccountRequest {
    private String cpf;
    private String name;
    private String rg;
    private LocalDate birthDate;
    private ClientAddressRequest address;
    private ClientPersonRequest person;
    private ClientPhoneRequest prone;
    private VehicleRequest vehicle;

}