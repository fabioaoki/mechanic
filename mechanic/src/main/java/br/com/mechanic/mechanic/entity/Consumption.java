package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumption")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "exemption_id")
    private Long exemptionId;
    @Column(name = "fee_value_id")
    private Long feeValueId;
    @Column(name = "transaction_ref")
    private String transactionRef;
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    private Boolean reversal;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
