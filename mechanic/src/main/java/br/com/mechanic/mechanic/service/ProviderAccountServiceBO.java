package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderAccountTypeException;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.exception.ProviderPhoneException;
import br.com.mechanic.mechanic.service.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.service.request.ProviderAccountUpdateRequestDto;
import br.com.mechanic.mechanic.service.response.ProviderAccountResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderAccountServiceBO {
    ProviderAccountResponseDto findById(Long id);

    Page<ProviderAccountResponseDto> findAll(final Pageable pageable);

    ProviderAccountResponseDto save(ProviderAccountRequestDto providerAccount) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException;

    void changeStatus(Long id, ProviderAccountStatusEnum statusEnum) throws ProviderAccountException;

    ProviderAccountResponseDto updateProviderAccount(Long id, ProviderAccountUpdateRequestDto requestDto);
}