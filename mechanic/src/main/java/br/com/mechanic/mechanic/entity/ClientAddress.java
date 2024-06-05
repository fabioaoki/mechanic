package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_address")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_account_id")
    private Long clientAccountId;
    private String city;
    private String street;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    private double latitude;
    private double longitude;
    private String state;
    private String number;
    private String neighborhood;
}
