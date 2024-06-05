package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderAddressRepositoryImpl {

    Page<ProviderAddress> findAll(Pageable pageable);

    Optional<ProviderAddress> findById(Long id);

    Optional<ProviderAddress> findByAddress(String city, String street, String zipcode, String state, String number, String neighborhood);

    ProviderAddress save(ProviderAddress entity);

    Page<ProviderAddress> findByProviderAccountId(Pageable pageable, Long providerAccountId);
}
