package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<EquipmentOut> findByProviderAccountAndEquipmentId(Long providerAccountId, Long id, LocalDateTime createDate) {
        return repository.findByProviderAccountIdAndEquipmentIdAndCreateDateGreaterThanEqualAndReversalIsFalse(providerAccountId, id, createDate);
    }

    @Override
    public Long countByProviderAccountIdAndEquipmentId(Long providerAccountId, Long equipmentId) {
        return repository.countByProviderAccountIdAndEquipmentIdAndReversalIsFalse(providerAccountId, equipmentId);
    }

}