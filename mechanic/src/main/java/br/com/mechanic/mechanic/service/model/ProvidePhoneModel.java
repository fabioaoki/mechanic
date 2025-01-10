package br.com.mechanic.mechanic.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProvidePhoneModel {

    private Long id;
    private Long providerAccountId;
    private Long providerPersonId;
    private Long area;
    private String number;
    private LocalDateTime createDate;
}