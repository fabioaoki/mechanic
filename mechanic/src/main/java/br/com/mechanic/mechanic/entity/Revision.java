package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "transaction_id")
    private Long transactionId;
    @Column(name = "provider_service_id")
    private Long providerServiceId;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "return_date")
    private LocalDateTime returnDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
