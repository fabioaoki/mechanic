package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.response.CompletedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompletedServiceManagerBO {
    CompletedResponseDto save(final CompletedServiceRequest completedServiceRequest);

    Page<CompletedResponseDto> findAll(final Pageable pageable);

    CompletedResponseDto findById(final Long id);

    Page<CompletedResponseDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    CompletedResponseDto updateCompletedService(final Long id, final CompletedServiceRequest requestDto);
}
