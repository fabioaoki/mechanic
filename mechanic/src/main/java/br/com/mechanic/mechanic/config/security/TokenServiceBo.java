package br.com.mechanic.mechanic.config.security;

import org.springframework.security.core.Authentication;

public interface TokenServiceBo {
    String generated(Authentication authentication);

    boolean isValidToken(String token);

    Long getIdProvider(String token);
}
