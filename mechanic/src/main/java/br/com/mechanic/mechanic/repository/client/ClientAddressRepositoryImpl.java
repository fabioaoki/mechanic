package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientAddressRepositoryImpl {

    Page<ClientAddress> findAll(Pageable pageable);

    Optional<ClientAddress> findById(Long id);

    Optional<ClientAddress> findByAddress(String city, String street, String zipcode, String state, String number, String neighborhood);

    ClientAddress save(ClientAddress entity);

    Optional<ClientAddress> findByClientAccountId(Long clientAccount);
}
