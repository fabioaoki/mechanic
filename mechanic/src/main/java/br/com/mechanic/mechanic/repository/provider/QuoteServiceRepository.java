package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.QuoteServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteServiceRepository extends JpaRepository<QuoteServiceEntity, Long> {

    Page<QuoteServiceEntity> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);
}