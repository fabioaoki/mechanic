package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderPhoneRepository extends JpaRepository<ProviderPhone, Long> {
    Optional<ProviderPhone> findByNumber(String number);
}
