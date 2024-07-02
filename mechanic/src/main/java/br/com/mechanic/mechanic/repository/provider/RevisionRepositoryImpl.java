package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface RevisionRepositoryImpl {

    Page<Revision> findAll(Pageable pageable);

    Optional<Revision> findById(Long id);

    Revision save(Revision entity);

    Page<Revision> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);
    Page<Revision> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

    void updateReturn(Long id, LocalDate revisionReturn);

    Optional<Revision> findByCompletedServiceId(Long completedServiceId);
}
