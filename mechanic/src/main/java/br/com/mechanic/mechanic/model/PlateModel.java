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
public class PlateModel {

    private Long id;
    private Long clientAccountId;
    private String plate;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}