package br.com.mechanic.mechanic.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeEfficiencyDto {
    private String employeeName;
    private Long servicesCompleted;
    private BigDecimal averageServiceValue;
}
