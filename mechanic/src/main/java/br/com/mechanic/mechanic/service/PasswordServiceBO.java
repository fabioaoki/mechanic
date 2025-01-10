package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.service.request.PasswordRequestDto;
import br.com.mechanic.mechanic.service.response.PasswordResponse;

public interface PasswordServiceBO {

    PasswordResponse findById(final Long id);

    PasswordResponse findByProviderAccountId(final Long providerAccountId);

    void updateProviderPassword(final Long providerAccountId, final PasswordRequestDto newPassword);

    void save(final Long providerAccountId, final String password, final String login);

    boolean matches(final Long providerAccountId, final String password);
}