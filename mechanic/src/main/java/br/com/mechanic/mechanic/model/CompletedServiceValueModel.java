
package br.com.mechanic.mechanic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CompletedServiceValueModel {

    private Long employeeAccountId;
    private Long providerServiceId;
    private Long quantity;
    private long quantityRevised;
    private BigDecimal mileageForInspection;
    private LocalDate endDate;
    private Long completedServiceId;
}
