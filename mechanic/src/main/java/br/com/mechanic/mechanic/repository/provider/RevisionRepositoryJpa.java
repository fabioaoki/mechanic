package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import br.com.mechanic.mechanic.response.RevisionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RevisionRepositoryJpa implements RevisionRepositoryImpl {

    private final RevisionRepository repository;

    public RevisionRepositoryJpa(RevisionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Revision save(Revision entity) {
        entity.setStartDate(LocalDate.now());
        return repository.save(entity);
    }

    @Override
    public Page<Revision> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Revision> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public Optional<Revision> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<Revision> findAllByClientAccountId(Pageable pageable, Long clientAccountId) {
        return repository.findAllByClientAccountId(pageable, clientAccountId);
    }

    @Override
    public void updateReturn(Long id, LocalDate revisionReturn, boolean isFinish, long quantityRevised) {
        repository.updateReturn(id, revisionReturn, isFinish, quantityRevised);
    }

    @Override
    public Optional<Revision> findByCompletedServiceId(Long completedServiceId) {
        return repository.findByCompletedServiceId(completedServiceId);
    }

    @Override
    public List<RevisionDto> findPendingRevision() {
        List<Object[]> results = repository.findPendingRevision();
        return results.stream()
                .map(result -> new RevisionDto(
                        ((Integer) result[0]).longValue(),    // id
                        ((Integer) result[1]).longValue(),    // completed_service_id
                        (String) result[2],                  // description
                        ((java.sql.Date) result[3]).toLocalDate(),  // start_date
                        result[4] != null ? ((java.sql.Date) result[4]).toLocalDate() : null,  // return_date
                        ((java.sql.Date) result[5]).toLocalDate(),  // end_date
                        ((Integer) result[6]).longValue(),   // quantity
                        result[7] != null ? ((java.sql.Date) result[7]).toLocalDate() : null,  // expected_return_date
                        (String) result[8],                  // client_phone
                        (String) result[9],                  // client_name
                        (String) result[10],                 // provider_phone
                        (String) result[11],                 // workshop
                        (String) result[12],                 // sid
                        (String) result[13]                  // token
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void updateNotification(List<Long> revisionIds) {
        repository.updateNotification(revisionIds, LocalDate.now());
    }

    @Override
    public void reversal(Long id) {
        repository.reversal(id, LocalDateTime.now());
    }

    @Override
    public void partialReversal(Long id, Long partialReversalValue) {
        repository.partialReversal(id, partialReversalValue, LocalDateTime.now());
    }
}