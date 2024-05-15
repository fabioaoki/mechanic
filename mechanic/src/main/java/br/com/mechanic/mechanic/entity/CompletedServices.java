package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "completed_services")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class CompletedServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "provider_person_id")
    private Long providerPersonId;
    @Column(name = "employee_account_id")
    private Long employeeAccountId;
    @Column(name = "provider_service_id")
    private Long providerServiceId;
    @Column(name = "vehicle_type_id")
    private Long vehicleTypeId;
    @Column(name = "color_id")
    private Long colorId;
    private String plate;
    @Column(name = "vehicle_name")
    private String vehicleName;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
