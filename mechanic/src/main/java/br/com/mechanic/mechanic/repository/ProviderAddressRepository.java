package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderAddressRepository extends JpaRepository<ProviderAddress, Long> {
    Optional<ProviderAddress> findByCityAndStreetAndZipCode(String city, String street, String zipcode);
}