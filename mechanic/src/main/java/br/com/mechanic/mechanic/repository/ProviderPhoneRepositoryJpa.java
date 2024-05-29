package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPhone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProviderPhoneRepositoryJpa implements ProviderPhoneRepositoryImpl {

    private final ProviderPhoneRepository repository;

    public ProviderPhoneRepositoryJpa(ProviderPhoneRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProviderPhone save(ProviderPhone entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<ProviderPhone> findByPhone(String number) {
        return repository.findByNumber(number);
    }

    @Override
    public Page<ProviderPhone> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ProviderPhone> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<ProviderPhone> findByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(providerAccountId, pageable);
    }
}
