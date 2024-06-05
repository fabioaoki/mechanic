package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientAccountRepositoryImpl {

    Page<ClientAccount> findAll(Pageable pageable);

    Optional<ClientAccount> findById(Long id);

    ClientAccount save(ClientAccount entity);

    Optional<ClientAccount> findByCpf(String cpf);

    Optional<ClientAccount> findByCpfOrRg(String cpf, String rh);


    Optional<ClientAccount> findByRg(String rg);
}
