package br.com.mechanic.mechanic.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaign")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "merchant_id")
    private Long merchantId;
    @Column(name = "client_account_id")
    private Long clientAccountId;
    private String name;
    private String description;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    private Boolean status;
    private String extras;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
