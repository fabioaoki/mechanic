package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.request.ClientAccountRequest;
import br.com.mechanic.mechanic.request.ClientAccountUpdateRequest;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.ClientAccountResponseDto;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDtoPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientAccountServiceBO {
    ClientAccountResponseDto save(ClientAccountRequest clientAccountRequest);

    Page<ClientAccountResponseDto> findAll(final Pageable pageable);

    ClientAccountResponseDto findById(Long id);

    ClientAccountResponseDto updateClientAccount(Long id, ClientAccountUpdateRequest requestDto);
}
