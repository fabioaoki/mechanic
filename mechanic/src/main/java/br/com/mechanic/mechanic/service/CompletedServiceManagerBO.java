package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.response.CompletedResponseDto;
import br.com.mechanic.mechanic.response.CompletedResponseDtoDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompletedServiceManagerBO {
    CompletedResponseDto save(final CompletedServiceRequest completedServiceRequest);

    Page<CompletedResponseDtoDefault> findAll(final Pageable pageable);

    CompletedResponseDtoDefault findById(final Long id);

    Page<CompletedResponseDtoDefault> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);
}
