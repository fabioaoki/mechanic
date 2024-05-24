package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderAccountTypeException;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.exception.ProviderPhoneException;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderAccountServiceBO {
    ProviderAccountResponseDto findById(Long id);

    Page<ProviderAccountResponseDto> findAll(final Pageable pageable);

    ProviderAccountResponseDto save(ProviderAccountRequestDto providerAccount) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException;

    void delete(Long id) throws ProviderAccountException;
}
