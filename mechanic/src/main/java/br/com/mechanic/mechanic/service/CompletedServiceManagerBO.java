package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.response.CompletedResponseByProviderAccountDto;
import br.com.mechanic.mechanic.response.CompletedResponseDto;
import br.com.mechanic.mechanic.response.CompletedResponseDtoDefault;
import br.com.mechanic.mechanic.response.EmployeeServiceCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CompletedServiceManagerBO {
    CompletedResponseDto save(final CompletedServiceRequest completedServiceRequest);

    List<EmployeeServiceCountDto> getCompletedServiceCountByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    Page<CompletedResponseDtoDefault> findAll(final Pageable pageable);

    CompletedResponseDtoDefault findById(final Long id);

    Page<CompletedResponseByProviderAccountDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable, final LocalDate startDate, final LocalDate endDate);

    Page<CompletedResponseDto> findAllByClientAccountId(final Long providerAccountId, final Pageable pageable);
}
