package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EquipmentOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class EquipmentOutRepositoryJpa implements EquipmentOutRepositoryImpl {

    EquipmentOutRepository repository;

    public EquipmentOutRepositoryJpa(EquipmentOutRepository repository) {
        this.repository = repository;
    }

    @Override
    public EquipmentOut save(EquipmentOut entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<EquipmentOut> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<EquipmentOut> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public Optional<EquipmentOut> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Long countByProviderAccountIdAndEquipmentId(Long providerAccountId, Long equipmentId) {
        return repository.countByProviderAccountIdAndEquipmentIdAndReversalIsFalse(providerAccountId, equipmentId);
    }

}