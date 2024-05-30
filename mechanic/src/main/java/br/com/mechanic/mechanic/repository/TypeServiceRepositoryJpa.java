package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.TypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class TypeServiceRepositoryJpa implements TypeServiceRepositoryImpl {

    TypeServiceRepository repository;

    public TypeServiceRepositoryJpa(TypeServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public TypeService save(TypeService entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<TypeService> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<TypeService> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<TypeService> findByName(String name) {
        return repository.findByName(name);
    }
}
