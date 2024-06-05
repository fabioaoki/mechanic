package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ClientPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ClientPersonResponseDto;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPersonServiceBO {
    ClientPersonResponseDto save(ClientPersonRequest personRequest, Long id);

    Page<ClientPersonResponseDto> findAll(final Pageable pageable);

    ClientPersonResponseDto findById(Long id);

    ClientPersonResponseDto updateClientPerson(Long id, ClientPersonRequest requestDto);
}
