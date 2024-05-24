package br.com.mechanic.mechanic.entity;

import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_account_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ProviderAccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider_account_id")
    private Long providerAccountId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private ProviderAccountStatusEnum status;
}