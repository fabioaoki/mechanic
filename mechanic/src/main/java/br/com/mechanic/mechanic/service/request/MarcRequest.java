package br.com.mechanic.mechanic.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MarcRequest {
    private String name;
    private String model;
    private String version;
    private String year;
    private String transmissionType;
}
