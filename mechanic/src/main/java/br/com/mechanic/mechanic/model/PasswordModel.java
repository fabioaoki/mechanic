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
public class PasswordModel {

    private Long id;
    private Long providerAccountId;
    private String password;
    private String oldPassword;
    private String login;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
}