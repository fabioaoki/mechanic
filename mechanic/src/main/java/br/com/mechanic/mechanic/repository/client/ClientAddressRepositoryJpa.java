package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ClientAddressRepositoryJpa implements ClientAddressRepositoryImpl {

    private final ClientAddressRepository repository;

    @Autowired
    public ClientAddressRepositoryJpa(ClientAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ClientAddress> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ClientAddress> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ClientAddress> findByAddress(String city, String street, String zipCode, String state, String number, String neighborhood) {
        return repository.findByCityAndStreetAndZipCodeAndStateAndNumberAndNeighborhood(city, street, zipCode, state, number, neighborhood);
    }

    @Override
    public ClientAddress save(ClientAddress entity) {
        if (entity.getCreateDate() == null) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<ClientAddress> findByClientAccountId(Long clientAccountId) {
        return repository.findByClientAccountId(clientAccountId);
    }
}