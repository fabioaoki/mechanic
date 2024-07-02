package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {
    Page<Revision> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    Page<Revision> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

    @Modifying
    @Transactional
    @Query("UPDATE Revision r SET r.returnDate = :revisionReturn WHERE r.id = :id")
    void updateReturn(Long id, LocalDate revisionReturn);

    Optional<Revision> findByCompletedServiceId(Long completedServiceId);
}
