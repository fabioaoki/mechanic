package br.com.mechanic.mechanic.entity.provider;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "completed_service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class CompletedService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "employee_account_id")
    private Long employeeAccountId;
    @Column(name = "provider_service_id")
    private Long providerServiceId;
    @Column(name = "vehicle_type_id")
    private Long vehicleTypeId;
    @Column(name = "color_id")
    private Long colorId;
    @Column(name = "plate_id")
    private Long plateId;
    @Column(name = "model_id")
    private Long modelId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    private BigDecimal amount;
}
