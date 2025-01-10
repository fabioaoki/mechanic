package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.QuoteServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuoteServiceRepositoryImpl {
    Page<QuoteServiceEntity> findAll(Pageable pageable);

    Optional<QuoteServiceEntity> findById(Long id);

    QuoteServiceEntity save(QuoteServiceEntity entity);

    Page<QuoteServiceEntity> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

}
