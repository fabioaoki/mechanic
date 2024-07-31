package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.response.EmployeeServiceCountDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountCompletedServiceGroupByDateDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountGroupByDateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompletedServiceRepositoryImpl {
    Page<CompletedService> findAll(Pageable pageable);

    Optional<CompletedService> findById(Long id);

    CompletedService save(CompletedService entity);

    Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId, LocalDate startDate, LocalDate endDate);


    List<EmployeeServiceCountDto> countFirstCompletedServiceByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountDto> countFirstCompletedServiceByProviderService(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountGroupByDateDto> countFirstCompletedServiceByProviderServiceGroupByDate(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountCompletedServiceGroupByDateDto> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    void setTransactionIds(List<Long> completedServiceIds, Long transactionId);

    void partialReversal(final Long id, final Long partialReversal);
}
