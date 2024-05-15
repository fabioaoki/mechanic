package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pack_service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class PackService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_pack_id")
    private Long providerPackId;
    @Column(name = "pack_id")
    private Long packId;
    @Column(name = "service_id")
    private Long serviceId;
    @Column(name = "exemption_id")
    private Long exemptionId;
    @Column(name = "service_fee_id")
    private Long serviceFeeId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
