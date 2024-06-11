package br.com.mechanic.mechanic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientPhoneModel {

    private Long id;
    private Long clientAccountId;
    private Long area;
    private String number;
    private LocalDateTime createDate;
}