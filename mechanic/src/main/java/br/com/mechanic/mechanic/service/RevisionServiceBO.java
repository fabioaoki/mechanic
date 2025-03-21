package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.service.request.RevisionRequest;
import br.com.mechanic.mechanic.service.response.RevisionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface RevisionServiceBO {
    RevisionResponse save(final RevisionRequest transactionRequest);

    Page<RevisionResponse> findAll(final Pageable pageable);

    RevisionResponse findById(final Long id);

    RevisionResponse findByCompletedServiceId(final Long id);

    Page<RevisionResponse> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    Page<RevisionResponse> findAllByClientAccountId(final Long clientAccountId, final Pageable pageable);

    void updateRevision(Long id, LocalDate revisionReturn, boolean isFinish, long quantityRevised);

    void senRevisionNotification();

    void reversal(Long id);

    void partialReversal(Long id, Long partialReversalValue);
}
