package br.com.mechanic.mechanic.entity.pack;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_fee")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pack_id")
    private Long packId;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "fee_id")
    private Long feeId;
    @Column(name = "fee_value_id")
    private Long feeValueId;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
