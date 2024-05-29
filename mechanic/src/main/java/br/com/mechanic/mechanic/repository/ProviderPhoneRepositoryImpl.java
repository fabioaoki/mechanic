package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import br.com.mechanic.mechanic.entity.ProviderPhone;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderPhoneRepositoryImpl {
    ProviderPhone save(ProviderPhone providerPhone);

    Optional<ProviderPhone> findByPhone(String number);

    Page<ProviderPhone> findAll(Pageable pageable);

    Optional<ProviderPhone> findById(Long id);

    Page<ProviderPhone> findByProviderAccountId(Pageable pageable, Long providerAccountId);
}
