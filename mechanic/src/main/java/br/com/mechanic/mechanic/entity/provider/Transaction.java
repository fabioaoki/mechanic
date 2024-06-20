package br.com.mechanic.mechanic.entity.provider;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "completed_services_ids")
    private String completedServiceIds;
    @Column(name = "vehicle_type_id")
    private Long vehicleTypeId;
    @Column(name = "plate_id")
    private Long plateId;
    @Column(name = "color_id")
    private Long colorId;
    @Column(name = "client_account_id")
    private Long clientAccountId;
    @Column(name = "reward_id")
    private Long rewardId;
    @Column(name = "vehicle_name")
    private String vehicleName;
    @Column(name = "workmanship_amount")
    private BigDecimal workmanshipAmount;
    private BigDecimal amount;
    private Long installments;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
