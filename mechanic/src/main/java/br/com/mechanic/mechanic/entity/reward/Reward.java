package br.com.mechanic.mechanic.entity.reward;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float amount;
    @Column(name = "campaign_id")
    private Long campaignId;
    @Column(name = "reward_type_id")
    private Long rewardTypeId;
    @Column(name = "reward_limit")
    private Integer rewardLimit;
    private String code;
    private Boolean status;
    private String title;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
