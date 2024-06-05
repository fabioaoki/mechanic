package br.com.mechanic.mechanic.entity.provider.emloyee;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class EmployeeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_account_id")
    private Long employeeAccountId;
    @Column(name = "completed_service_id")
    private Long completedServiceId;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "type_service_id")
    private Long typeServiceId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
