package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.TransactionRequest;
import br.com.mechanic.mechanic.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface TransactionServiceBO {
    TransactionResponse save(final TransactionRequest transactionRequest);

    Page<TransactionResponse> findAll(final Pageable pageable);

    TransactionResponse findById(final Long id);

    Page<TransactionResponse> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    Page<TransactionResponse> findAllByClientAccountId(final Long clientAccountId, final Pageable pageable);

    void reversal(final BigDecimal totalAmount, final BigDecimal workmanshipAmount, final Long id);
}
