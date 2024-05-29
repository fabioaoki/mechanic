package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderAddressRepository extends JpaRepository<ProviderAddress, Long> {
    Optional<ProviderAddress> findByCityAndStreetAndZipCodeAndStateAndNumberAndNeighborhood(String city, String street, String zipcode, String state, String number, String neighborhood);

    Page<ProviderAddress> findAllByProviderAccountId(Long providerAccountId, Pageable pageable);
}