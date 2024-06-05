package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderPersonRepositoryImpl {

    Page<ProviderPerson> findAll(Pageable pageable);

    Optional<ProviderPerson> findById(Long id);

    ProviderPerson save(ProviderPerson entity);
}
