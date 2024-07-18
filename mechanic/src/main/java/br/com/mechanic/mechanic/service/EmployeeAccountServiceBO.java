package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDtoPage;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeAccountServiceBO {
    EmployeeAccountResponseDto save(final EmployeeAccountRequest personRequest);

    Page<EmployeeAccountResponseDtoPage> findAll(final Pageable pageable);

    EmployeeAccountResponseDto findById(final Long id);

    Page<EmployeeAccountResponseDtoPage> findByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    EmployeeAccountResponseDto updateEmployeeAccount(final Long id, final EmployeeAccountRequest requestDto);
}
