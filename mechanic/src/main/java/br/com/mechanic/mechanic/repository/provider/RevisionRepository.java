package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {
    Page<Revision> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    Page<Revision> findAllByClientAccountId(Pageable pageable, Long clientAccountId);
}
