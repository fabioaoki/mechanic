package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.request.ClientPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ClientPersonResponseByControllerDto;
import br.com.mechanic.mechanic.response.ClientPersonResponseDto;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPersonServiceBO {
    ClientPersonResponseDto save(ClientPersonRequest personRequest, Long id);

    Page<ClientPersonResponseByControllerDto> findAll(final Pageable pageable);

    ClientPersonResponseByControllerDto findById(Long id);

    ClientPersonResponseByControllerDto findByClientAccountId(Long id);

    ClientPersonResponseByControllerDto updateClientPerson(Long id, ClientPersonRequest requestDto);
}
