package br.com.mechanic.mechanic.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ServicePeriodDto {
    private String period;
    private Long totalServices;
}
