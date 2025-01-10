package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.service.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompletedServiceRepositoryImpl {
    Page<CompletedService> findAll(Pageable pageable);

    Optional<CompletedService> findById(Long id);

    CompletedService save(CompletedService entity);

    Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId, LocalDate startDate, LocalDate endDate, boolean isReversal);


    List<EmployeeServiceCountDto> countFirstCompletedServiceByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountDto> countFirstCompletedServiceByProviderService(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountGroupByDateDto> countFirstCompletedServiceByProviderServiceGroupByDate(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<ProviderServiceCountCompletedServiceGroupByDateDto> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    void setTransactionIds(List<Long> completedServiceIds, Long transactionId);

    void partialReversal(final Long id, final Long partialReversal);

    //////
    List<ServicePeriodDto> countServicesByMonth(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<RevenueByServiceTypeDto> getTotalRevenueByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<EquipmentUtilizationDto> getEquipmentUtilization(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<EmployeeEfficiencyDto> getEmployeeEfficiency(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    List<LaborCostDto> getLaborCostByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    // Method for Equipment Cost by Service
    List<EquipmentCostDto> getEquipmentCostByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    // Method for Labor Cost by Employee
    List<LaborCostDto> getLaborCostByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    // Method for Cost Revenue Comparison
    List<CostRevenueComparisonDto> getCostRevenueComparison(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    // Method for Inventory Efficiency
    List<EquipmentInByProviderAccountIdSumTotalDto> getInventoryEfficiency(Long providerAccountId, LocalDate startDate, LocalDate endDate);

    MaintenanceRevisionsDto getMaintenanceAndRevisionsReport(Long providerAccountId, LocalDate startDate, LocalDate endDate);
}
