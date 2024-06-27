package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.response.CompletedResponseByProviderAccountDto;
import br.com.mechanic.mechanic.response.CompletedResponseDto;
import br.com.mechanic.mechanic.response.CompletedResponseDtoDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface CompletedServiceManagerBO {
    CompletedResponseDto save(final CompletedServiceRequest completedServiceRequest);

    Page<CompletedResponseDtoDefault> findAll(final Pageable pageable);

    CompletedResponseDtoDefault findById(final Long id);

    Page<CompletedResponseByProviderAccountDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable, final LocalDate startDate, final LocalDate endDate);

    Page<CompletedResponseDto> findAllByClientAccountId(final Long providerAccountId, final Pageable pageable);
}
