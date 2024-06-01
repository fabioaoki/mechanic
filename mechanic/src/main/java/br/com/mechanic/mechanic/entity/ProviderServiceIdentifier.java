package br.com.mechanic.mechanic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider_service_identifier")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class ProviderServiceIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
}
