package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import br.com.mechanic.mechanic.entity.provider.ProviderPhone;
import br.com.mechanic.mechanic.repository.provider.ProviderPhoneRepository;
import br.com.mechanic.mechanic.repository.provider.ProviderPhoneRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class ClientPhoneRepositoryJpa implements ClientPhoneRepositoryImpl {

    private final ClientPhoneRepository repository;

    public ClientPhoneRepositoryJpa(ClientPhoneRepository repository) {
        this.repository = repository;
    }

    @Override
    public ClientPhone save(ClientPhone entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<ClientPhone> findByPhone(String number) {
        return repository.findByNumber(number);
    }

    @Override
    public Page<ClientPhone> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ClientPhone> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ClientPhone> findByClientAccountId(Long clientAccountId) {
        return repository.findByClientAccountId(clientAccountId);
    }
}
