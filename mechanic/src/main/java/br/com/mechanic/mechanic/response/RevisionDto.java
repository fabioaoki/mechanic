package br.com.mechanic.mechanic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RevisionDto {
    private Long id;
    private Long completedServiceId;
    private String description;
    private LocalDate startDate;
    private LocalDate returnDate;
    private LocalDate endDate;
    private Long quantity;
    private LocalDate expectedReturnDate;
    private String clientPhone;
    private String clientName;
    private String providerPhone;
    private String workshop;
    private String sid;
    private String token;
}
