package br.com.mechanic.mechanic.entity.vehicle;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "revision")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "completed_service_id")
    private Long completedServiceId;
    @Column(name = "provider_service_id")
    private Long providerServiceId;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "client_account_id")
    private Long clientAccountId;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "return_date")
    private LocalDate returnDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    private BigDecimal mileage;
    @Column(name = "mileage_for_inspection")
    private BigDecimal mileageForInspection;
    private boolean finish;
    private Long quantity;
    @Column(name = "revision_id")
    private Long revisionId;
}
