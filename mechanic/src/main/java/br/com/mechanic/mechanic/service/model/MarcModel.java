package br.com.mechanic.mechanic.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MarcModel {

    private Long id;
    private String name;
    private String model;
    private String version;
    private String year;
    private String transmissionType;
    private LocalDateTime createDate;
}