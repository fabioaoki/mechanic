package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderAddressRepositoryImpl {

    Page<ProviderAddress> findAll(Pageable pageable);

    Optional<ProviderAddress> findById(Long id);

    Optional<ProviderAddress> findByAddress(String city, String street, String zipcode, String state, String number, String neighborhood);

    ProviderAddress save(ProviderAddress entity);
}
