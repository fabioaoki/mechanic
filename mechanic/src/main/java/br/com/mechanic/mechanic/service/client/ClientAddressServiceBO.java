package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.service.request.ClientAddressRequest;
import br.com.mechanic.mechanic.service.response.ClientAddressResponseByControllerDto;
import br.com.mechanic.mechanic.service.response.ClientAddressResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientAddressServiceBO {
    ClientAddressResponseDto save(ClientAddressRequest addressRequest, Long clientAccountId);

    Page<ClientAddressResponseByControllerDto> findAll(final Pageable pageable);

    ClientAddressResponseByControllerDto findByClientAccountId(Long id);

    ClientAddressResponseByControllerDto findById(Long id);

    ClientAddressResponseByControllerDto updateClientAddress(Long id, ClientAddressRequest addressRequest);

    Optional<ClientAddressResponseByControllerDto> findByProviderAccountId(Long clientAccount);
}
