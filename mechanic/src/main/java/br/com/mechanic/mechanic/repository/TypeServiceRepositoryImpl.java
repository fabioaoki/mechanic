package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.TypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TypeServiceRepositoryImpl {
    Optional<TypeService> findByName(String name);

    TypeService save(TypeService typeService);

    Page<TypeService> findAll(Pageable pageable);

    Optional<TypeService> findById(Long id);
}
