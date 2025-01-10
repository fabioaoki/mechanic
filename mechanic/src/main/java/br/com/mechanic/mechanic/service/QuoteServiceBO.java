package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.service.request.QuoteServiceRequest;
import br.com.mechanic.mechanic.service.response.CompletedResponseByProviderAccountDto;
import br.com.mechanic.mechanic.service.response.CompletedResponseDto;
import br.com.mechanic.mechanic.service.response.CompletedResponseDtoDefault;
import br.com.mechanic.mechanic.service.response.QuoteServiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface QuoteServiceBO {
    QuoteServiceResponseDto save(final QuoteServiceRequest quoteServiceRequest, final Long providerAccountId);


//    Page<CompletedResponseDtoDefault> findAll(final Pageable pageable);
//
//    CompletedResponseDtoDefault findById(final Long id);
//
//    Page<CompletedResponseByProviderAccountDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable, final LocalDate startDate, final LocalDate endDate);
//
//    Page<CompletedResponseDto> findAllByClientAccountId(final Long providerAccountId, final Pageable pageable);

}
