package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_return")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "completed_service_id")
    private Long completedServiceId;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "provider_service_id")
    private Long providerServiceId;
    @Column(name = "client_account_id")
    private Long clientAccountId;
    @Column(name = "transaction_id")
    private Long transactionId;
    @Column(name = "forecast_return_date")
    private LocalDateTime forecastReturnDate;
    @Column(name = "return_date")
    private LocalDateTime returnDate;
    private Boolean serviceReturn;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
