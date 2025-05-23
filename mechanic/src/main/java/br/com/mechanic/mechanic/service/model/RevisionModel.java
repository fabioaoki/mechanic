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
public class RevisionModel {

    private Long id;
    private Long completedServiceId;
    private Long providerServiceId;
    private Long providerAccountId;
    private Long clientAccountId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnDate;
    private Boolean isDeleted;
    private LocalDate expectedReturnDate;
    private boolean notification;
    private LocalDate notificationDate;
    private LocalDateTime lastUpdate;
}
