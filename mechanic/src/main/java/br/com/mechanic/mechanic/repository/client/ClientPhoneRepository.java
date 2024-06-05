package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPhoneRepository extends JpaRepository<ClientPhone, Long> {}
