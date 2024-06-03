package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_in")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class EquipmentIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "equipment_id")
    private Long equipmentId;
    private BigDecimal amount;
    private Long quantity;
    private boolean finish;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
