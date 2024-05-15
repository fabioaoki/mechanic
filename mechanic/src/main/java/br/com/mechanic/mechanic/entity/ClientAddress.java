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
    @Column(name = "post_code")
    private String postCode;
    @Column(name = "address_1")
    private String address1;
    @Column(name = "address_2")
    private String address2;
    private String city;
    private String country;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
