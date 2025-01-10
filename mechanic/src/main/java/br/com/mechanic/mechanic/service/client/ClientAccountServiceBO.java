package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.service.request.ClientAccountRequest;
import br.com.mechanic.mechanic.service.request.ClientAccountUpdateRequest;
import br.com.mechanic.mechanic.service.response.ClientAccountResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientAccountServiceBO {
    ClientAccountResponseDto save(ClientAccountRequest clientAccountRequest);

    Page<ClientAccountResponseDto> findAll(final Pageable pageable);

    ClientAccountResponseDto findById(Long id);

    ClientAccountResponseDto updateClientAccount(Long id, ClientAccountUpdateRequest requestDto);
}
