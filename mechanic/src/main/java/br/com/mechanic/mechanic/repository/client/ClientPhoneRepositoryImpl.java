package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientPhoneRepositoryImpl {
    ClientPhone save(ClientPhone providerPhone);

    Optional<ClientPhone> findByPhone(String number);

    Page<ClientPhone> findAll(Pageable pageable);

    Optional<ClientPhone> findById(Long id);

    Optional<ClientPhone> findByClientAccountId(Long providerAccountId);
}
