package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class EquipmentRepositoryJpa implements EquipmentRepositoryImpl {

    EquipmentRepository repository;

    public EquipmentRepositoryJpa(EquipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Equipment save(Equipment entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<Equipment> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Equipment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Equipment> findByName(String name) {
        return repository.findByName(name);
    }
}
