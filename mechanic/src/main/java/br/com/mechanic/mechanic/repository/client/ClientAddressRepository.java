package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientAddressRepository extends JpaRepository<ClientAddress, Long> {
    Optional<ClientAddress> findByCityAndStreetAndZipCodeAndStateAndNumberAndNeighborhood(String city, String street, String zipcode, String state, String number, String neighborhood);

    Optional<ClientAddress> findByClientAccountId(Long clientAccountId);
}
