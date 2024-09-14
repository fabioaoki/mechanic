package br.com.mechanic.mechanic.entity.provider;

import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ProviderAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String workshop;
    private String cnpj;
    private String email;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    private Long type;
    @Enumerated(EnumType.STRING)
    private ProviderAccountStatusEnum status = ProviderAccountStatusEnum.INITIAL_BLOCK;;
}
