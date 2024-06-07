package br.com.mechanic.mechanic.entity.vehicle;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "plate_id")
    private Long plateId;
    @Column(name = "client_account_id")
    private Long clientAccountId;
    @Column(name = "marcId")
    private Long marcId;
    @Column(name = "color_id")
    private Long colorId;
    private Boolean sold;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
