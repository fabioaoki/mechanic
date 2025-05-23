package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.service.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.service.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.service.response.EmployeeAccountResponseDtoPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeAccountServiceBO {
    EmployeeAccountResponseDto save(final EmployeeAccountRequest personRequest);

    Page<EmployeeAccountResponseDtoPage> findAll(final Pageable pageable);

    EmployeeAccountResponseDto findById(final Long id);

    Page<EmployeeAccountResponseDtoPage> findByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    EmployeeAccountResponseDto updateEmployeeAccount(final Long id, final EmployeeAccountRequest requestDto);
}
