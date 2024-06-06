package br.com.mechanic.mechanic.response;

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
public class MarcResponseDto {
    private Long id;
    private String name;
    private String model;
    private String version;
    private LocalDate year;
    private LocalDateTime createDate;
}
