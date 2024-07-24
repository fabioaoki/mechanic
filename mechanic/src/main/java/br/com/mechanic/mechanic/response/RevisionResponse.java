package br.com.mechanic.mechanic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class RevisionResponse {
    private Long id;
    private Long completedServiceId;
    private Long providerServiceId;
    private Long providerAccountId;
    private Long clientAccountId;
    private BigDecimal mileage;
    private BigDecimal mileageForInspection;
    private LocalDate startDate;
    private LocalDate endDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate returnDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isDeleted;
    private boolean finish;
    private Long revisionId;
    private Long quantity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate expectedReturnDate;
}