package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProviderAccountRepositoryJpa implements ProviderAccountRepositoryImpl {

    private final ProviderAccountRepository providerAccountRepository;

    @Autowired
    public ProviderAccountRepositoryJpa(ProviderAccountRepository providerAccountRepository) {
        this.providerAccountRepository = providerAccountRepository;
    }

    @Override
    public Page<ProviderAccount> findAll(Pageable pageable) {
        return providerAccountRepository.findAll(pageable);
    }

    @Override
    public Optional<ProviderAccount> findById(Long id) {
        return providerAccountRepository.findById(id);
    }

    @Override
    public Optional<ProviderAccount> findByCnpj(String cnpj) {
        return providerAccountRepository.findByCnpj(cnpj);
    }

    @Override
    public Optional<ProviderAccount> findByEmail(String email) {
        return providerAccountRepository.findByEmail(email);
    }

    @Override
    public ProviderAccount save(ProviderAccount entity) {
        if(entity.getCreateDate() == null){
            entity.setCreateDate(LocalDateTime.now());
        }
        return providerAccountRepository.save(entity);
    }
}
