package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompletedServiceRepository extends JpaRepository<CompletedService, Long> {
    Optional<CompletedService> findByProviderAccountId(Long providerAccountId);

    Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);
}

