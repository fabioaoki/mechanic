package br.com.mechanic.mechanic.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ModelResponseDto {
    private Long id;
    private String name;
    private String model;
    private String version;
    private String year;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String transmissionType;
    private LocalDateTime createDate;
}
