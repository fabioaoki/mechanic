package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPhone;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProviderPhoneRepositoryJpa implements ProviderPhoneRepositoryImpl {

    private final ProviderPhoneRepository repository;

    public ProviderPhoneRepositoryJpa(ProviderPhoneRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProviderPhone save(ProviderPhone entity) {
        entity.setCreateDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public Optional<ProviderPhone> findByPhone(String number) {
        return repository.findByNumber(number);
    }
}
