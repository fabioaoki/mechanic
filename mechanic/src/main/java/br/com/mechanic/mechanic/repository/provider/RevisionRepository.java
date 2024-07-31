package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {
    Page<Revision> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    Page<Revision> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

    @Modifying
    @Transactional
    @Query("UPDATE Revision r SET r.returnDate = :revisionReturn, r.finish = :isFinish, r.quantity = :quantityRevised WHERE r.id = :id")
    void updateReturn(Long id, LocalDate revisionReturn, boolean isFinish, long quantityRevised);

    Optional<Revision> findByCompletedServiceId(Long completedServiceId);

    @Modifying
    @Transactional
    @Query(value = "SELECT x.id, x.completed_service_id, psi.description, x.start_date, x.return_date, x.end_date, x.quantity, x.expected_return_date, cp.\"number\" as client_phone, cp2.\"name\", pp.\"number\" as provider_phone, pa.workshop, pwt.sid, pwt.\"token\" " +
            "FROM mechanic.revision x " +
            "INNER JOIN mechanic.client_phone cp ON x.client_account_id = cp.client_account_id " +
            "INNER JOIN mechanic.client_person cp2 ON x.client_account_id = cp2.client_account_id " +
            "INNER JOIN mechanic.provider_phone pp ON x.provider_account_id = pp.provider_account_id " +
            "INNER JOIN mechanic.provider_account pa ON x.provider_account_id = pa.id " +
            "INNER JOIN mechanic.provider_service ps ON x.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.provider_whatsapp_token pwt ON x.provider_account_id = pwt.provider_account_id " +
            "WHERE x.is_deleted = false AND x.finish = false AND x.notification = false " +
            "AND ((x.end_date >= CURRENT_DATE AND x.end_date <= CURRENT_DATE + INTERVAL '5 day') " +
            "OR (x.expected_return_date >= CURRENT_DATE AND x.expected_return_date <= CURRENT_DATE + INTERVAL '5 day'))", nativeQuery = true)
    List<Object[]> findPendingRevision();

    @Modifying
    @Transactional
    @Query("UPDATE Revision r SET r.notification = true, r.notificationDate = :notificationDate WHERE r.id IN (:revisionIds)")
    void updateNotification(@Param("revisionIds") List<Long> revisionIds, @Param("notificationDate") LocalDate notificationDate);

    @Modifying
    @Transactional
    @Query("UPDATE Revision r SET r.isDeleted = true, r.lastUpdate = :now WHERE r.id = :id")
    void reversal(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Revision r SET r.quantity = :partialReversalValue, r.lastUpdate = :now WHERE r.id = :id")
    void partialReversal(@Param("id") Long id, @Param("partialReversalValue") Long partialReversalValue, @Param("now") LocalDateTime now);
}


//    SELECT x.id , x.completed_service_id , psi.identifier , x.start_date , x.return_date , x.end_date  , x.quantity ,x.expected_return_date , cp."number" as client_phone , cp2."name" , pp."number" as provider_phone, pa.workshop , pwt.sid , pwt."token"
//        FROM mechanic.revision x
//        inner join mechanic.client_phone cp
//        on x.client_account_id = cp.client_account_id
//        inner join mechanic.client_person cp2
//        on x.client_account_id = cp2.client_account_id
//        inner join mechanic.provider_phone pp
//        on x.provider_account_id = pp.provider_account_id
//        inner join mechanic.provider_account pa
//        on x.provider_account_id = pa.id
//        inner join mechanic.provider_service ps
//        on x.provider_service_id = ps.id
//        inner join mechanic.provider_service_identifier psi
//        on ps.identifier_id = psi.id
//        inner join mechanic.provider_whatsap_token pwt
//        on x.provider_account_id = pwt.provider_account_id
//        WHERE x.is_deleted = false
//        AND x.finish = false
//        and x.notification = false
//        AND (
//        (x.end_date >= CURRENT_DATE AND x.end_date <= CURRENT_DATE + INTERVAL '5 day')
//        OR
//        (x.expected_return_date >= CURRENT_DATE AND x.expected_return_date <= CURRENT_DATE + INTERVAL '5 day')
//        );