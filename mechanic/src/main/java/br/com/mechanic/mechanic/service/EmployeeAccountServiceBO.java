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
    EmployeeAccountResponseDto save(EmployeeAccountRequest personRequest);

    Page<EmployeeAccountResponseDtoPage> findAll(final Pageable pageable);

    EmployeeAccountResponseDto findById(Long id);

    EmployeeAccountResponseDto updateEmployeeAccount(Long id, EmployeeAccountRequest requestDto);
}
