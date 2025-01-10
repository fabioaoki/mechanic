package br.com.mechanic.mechanic.entity.provider;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quote_service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class QuoteServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "quote_service_id")
    private Long quoteServiceId;
    private String plate;
    @Column(name = "vehicle_description")
    private String vehicleDescription;
    private String description;
    @Column(name = "client_name")
    private String clientName;
    private Float value;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
}