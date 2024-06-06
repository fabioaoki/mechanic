package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.request.ClientAddressRequest;
import br.com.mechanic.mechanic.response.ClientAddressResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientAddressServiceBO {
    ClientAddressResponseDto save(ClientAddressRequest addressRequest, Long clientAccountId);

    Page<ClientAddressResponseDto> findAll(final Pageable pageable);

    ClientAddressResponseDto findById(Long id);

    ClientAddressResponseDto updateClientAddress(Long id, ClientAddressRequest addressRequest);

    Optional<ClientAddressResponseDto> findByProviderAccountId(Long clientAccount);
}
