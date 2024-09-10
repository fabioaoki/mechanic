package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.PasswordRequestDto;
import br.com.mechanic.mechanic.response.PasswordResponse;

public interface PasswordServiceBO {

    PasswordResponse findById(final Long id);

    PasswordResponse findByProviderAccountId(final Long providerAccountId);

    void updateProviderPassword(final Long providerAccountId, final PasswordRequestDto newPassword);

    void save(final Long providerAccountId, final String password);

    boolean matches(final Long providerAccountId, final String password);
}