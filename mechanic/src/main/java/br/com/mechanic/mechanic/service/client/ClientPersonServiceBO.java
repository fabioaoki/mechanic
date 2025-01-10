package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.service.request.ClientPersonRequest;
import br.com.mechanic.mechanic.service.response.ClientPersonResponseByControllerDto;
import br.com.mechanic.mechanic.service.response.ClientPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPersonServiceBO {
    ClientPersonResponseDto save(ClientPersonRequest personRequest, Long id);

    Page<ClientPersonResponseByControllerDto> findAll(final Pageable pageable);

    ClientPersonResponseByControllerDto findById(Long id);

    ClientPersonResponseByControllerDto findByClientAccountId(Long id);

    ClientPersonResponseByControllerDto updateClientPerson(Long id, ClientPersonRequest requestDto);
}
