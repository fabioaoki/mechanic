package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ClientAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAddressRepository extends JpaRepository<ClientAddress, Long> {}
