package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.request.ClientPhoneRequest;
import br.com.mechanic.mechanic.response.ClientPhoneResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPhoneServiceBO {
    ClientPhoneResponseDto save(final ClientPhoneRequest phoneRequest, final Long clientAccountId);

    Page<ClientPhoneResponseDto> findAll(final Pageable pageable);

    ClientPhoneResponseDto findById(final Long id);

    ClientPhoneResponseDto findByClientAccountId(final Long clientAccountId);

    ClientPhoneResponseDto updateClientPhone(final Long id, final ClientPhoneRequest requestDto);
}
