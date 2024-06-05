package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VehicleRequest {

    private PlateRequest plate;
    private MarcRequest marc;
    private LocalDateTime year;
    private String name;
    private String model;
    private ColorCarRequest color;
}
