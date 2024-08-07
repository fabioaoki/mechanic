package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.request.ReversalCompletedServiceRequest;
import br.com.mechanic.mechanic.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CompletedServiceManagerBO {
    CompletedResponseDto save(final CompletedServiceRequest completedServiceRequest);

    List<EmployeeServiceCountDto> getCompletedServiceCountByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountDto> getCompletedServiceCountByProviderService(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountGroupByDateDto> countFirstCompletedServiceByProviderServiceGroupByDate(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountCompletedServiceGroupByDateDto> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    Page<CompletedResponseDtoDefault> findAll(final Pageable pageable);

    CompletedResponseDtoDefault findById(final Long id);

    Page<CompletedResponseByProviderAccountDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable, final LocalDate startDate, final LocalDate endDate);
    Page<CompletedResponseByProviderAccountDto> findAllReversalByProviderAccountId(final Long providerAccountId, final Pageable pageable, final LocalDate startDate, final LocalDate endDate);

    Page<CompletedResponseDto> findAllByClientAccountId(final Long providerAccountId, final Pageable pageable);

    void reversalCompletedService(final Long providerAccountId, final ReversalCompletedServiceRequest reversalRequest);

    List<ServicePeriodDto> getServicesByMonth(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<RevenueByServiceTypeDto> getRevenueByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<EquipmentUtilizationDto> getEquipmentUtilizationReport(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<EmployeeEfficiencyDto> getEmployeeEfficiencyReport(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<LaborCostDto> getLaborCostsByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<EquipmentCostDto> getEquipmentCostsByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<CostRevenueComparisonDto> getCostRevenueComparison(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<EquipmentInByProviderAccountIdSumTotalDto> getInventoryEfficiencyReport(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    MaintenanceRevisionsDto getMaintenanceAndRevisionsReport(Long providerAccountId, LocalDate startDate, LocalDate endDate);
}
