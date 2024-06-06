package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPhoneRepository extends JpaRepository<ClientPhone, Long> {
    Optional<ClientPhone> findByClientAccountId(Long clientAccountId);

    Optional<ClientPhone> findByNumber(String number);
}
