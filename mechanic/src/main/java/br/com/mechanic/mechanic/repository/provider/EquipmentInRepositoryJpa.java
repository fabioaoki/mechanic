package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class EquipmentInRepositoryJpa implements EquipmentInRepositoryImpl {

    EquipmentInRepository repository;

    public EquipmentInRepositoryJpa(EquipmentInRepository repository) {
        this.repository = repository;
    }

    @Override
    public EquipmentIn save(EquipmentIn entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<EquipmentIn> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<EquipmentIn> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public Optional<EquipmentIn> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<EquipmentIn> findByProviderAccountIdAndEquipmentId(Long providerAccountId, Long equipmentId) {
        return repository.findByProviderAccountIdAndEquipmentIdAndFinishIsFalse(providerAccountId, equipmentId);
    }

    @Override
    public Optional<EquipmentIn> findByProviderAccountId(Long providerAccountId) {
        return repository.findByProviderAccountId(providerAccountId);
    }
}