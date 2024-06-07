package br.com.mechanic.mechanic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VehicleModel {

    private Long id;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private boolean use;
}