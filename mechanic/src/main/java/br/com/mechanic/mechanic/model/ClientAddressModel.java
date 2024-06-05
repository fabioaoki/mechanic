package br.com.mechanic.mechanic.model;

import br.com.mechanic.mechanic.enuns.StateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientAddressModel {

    private Long id;
    private Long clientAccountId;
    private String city;
    private String street;
    private String zipCode;
    private double latitude;
    private double longitude;
    private StateEnum state;
    private String number;
    private String neighborhood;
    private LocalDateTime createDate;
}