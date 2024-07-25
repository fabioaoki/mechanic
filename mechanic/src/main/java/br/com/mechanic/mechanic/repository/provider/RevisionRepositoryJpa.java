package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public List<Revision> findPendingRevision() {
        return repository.findPendingRevision();
    }

}