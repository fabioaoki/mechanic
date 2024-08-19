package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.response.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component
public class CompletedServiceRepositoryJpa implements CompletedServiceRepositoryImpl {

    CompletedServiceRepository repository;

    public CompletedServiceRepositoryJpa(CompletedServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletedService save(CompletedService entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<CompletedService> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId, LocalDate startDate, LocalDate endDate, boolean isReversal) {
        if (isReversal) {
            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = null;
                LocalDateTime endDateTime = null;
                startDateTime = startDate.atTime(LocalTime.MIN);  // 00:00:00.000 do dia
                endDateTime = endDate.atTime(LocalTime.MAX);  // 23:59:59.999 do dia
                return repository.findByProviderAccountIdAndCreateDateBetween(providerAccountId, startDateTime, endDateTime, pageable, true);
            } else {
                return repository.findAllByProviderAccountId(pageable, providerAccountId, true);

            }
        } else {
            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = null;
                LocalDateTime endDateTime = null;
                startDateTime = startDate.atTime(LocalTime.MIN);  // 00:00:00.000 do dia
                endDateTime = endDate.atTime(LocalTime.MAX);  // 23:59:59.999 do dia
                return repository.findByProviderAccountIdAndCreateDateBetween(providerAccountId, startDateTime, endDateTime, pageable, false);
            } else {
                return repository.findAllByProviderAccountId(pageable, providerAccountId, false);

            }
        }
    }

    @Override
    public List<EmployeeServiceCountDto> countFirstCompletedServiceByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;
            startDateTime = startDate.atTime(LocalTime.MIN);
            endDateTime = endDate.atTime(LocalTime.MAX);
            List<Object[]> results = repository.countCompletedServicesByEmployeeAccountIdAndDate(providerAccountId, startDateTime, endDateTime);

            return results.stream()
                    .map(result -> new EmployeeServiceCountDto((String) result[0], (String) result[1], (String) result[2], (Long) result[3]))
                    .collect(Collectors.toList());
        } else {
            List<Object[]> results = repository.findServiceCountsByProviderAccount(providerAccountId);
            return results.stream()
                    .map(result -> new EmployeeServiceCountDto((String) result[0], (String) result[1], (String) result[2], (Long) result[3]))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ProviderServiceCountDto> countFirstCompletedServiceByProviderService(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;
            startDateTime = startDate.atTime(LocalTime.MIN);
            endDateTime = endDate.atTime(LocalTime.MAX);
            List<Object[]> results = repository.countCompletedServicesByProviderServiceIdAndDate(providerAccountId, startDateTime, endDateTime);

            return results.stream()
                    .map(result -> new ProviderServiceCountDto(
                            (String) result[0],
                            (String) result[1],
                            (Long) result[2],
                            BigDecimal.valueOf((Double) result[3]).setScale(2, RoundingMode.HALF_EVEN),
                            BigDecimal.valueOf((Double) result[4]).setScale(2, RoundingMode.HALF_EVEN)
                    ))
                    .collect(Collectors.toList());
        } else {
            List<Object[]> results = repository.countCompletedServicesByProviderServiceId(providerAccountId);
            return results.stream()
                    .map(result -> new ProviderServiceCountDto(
                            (String) result[0],
                            (String) result[1],
                            (Long) result[2],
                            BigDecimal.valueOf((Double) result[3]).setScale(2, RoundingMode.HALF_EVEN),
                            BigDecimal.valueOf((Double) result[4]).setScale(2, RoundingMode.HALF_EVEN)
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ProviderServiceCountGroupByDateDto> countFirstCompletedServiceByProviderServiceGroupByDate(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        startDateTime = startDate.atTime(LocalTime.MIN);
        endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.countCompletedServicesByProviderServiceIdAndDateAndGroupByDate(providerAccountId, startDateTime, endDateTime);

        return results.stream()
                .map(result -> new ProviderServiceCountGroupByDateDto(
                        (String) result[0],
                        (String) result[1],
                        (Long) result[2],
                        BigDecimal.valueOf((Double) result[3]).setScale(2, RoundingMode.HALF_EVEN),
                        BigDecimal.valueOf((Double) result[4]).setScale(2, RoundingMode.HALF_EVEN),
                        LocalDate.parse((String) result[5])
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderServiceCountCompletedServiceGroupByDateDto> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        startDateTime = startDate.atTime(LocalTime.MIN);
        endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.countCompletedServicesByVehicleTypeIdAndOptionalDate(providerAccountId, startDateTime, endDateTime);

        return results.stream()
                .map(result -> new ProviderServiceCountCompletedServiceGroupByDateDto(
                        (String) result[0],   // identifier
                        (String) result[1],   // typeOfCar
                        (Long) result[2],     // count
                        LocalDate.parse((String) result[3])  // date from String
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void setTransactionIds(List<Long> completedServiceIds, Long transactionId) {
        repository.setTransactionIds(completedServiceIds, transactionId);
    }

    @Override
    public void partialReversal(Long id, Long partialReversal) {
        repository.partialReversal(id, partialReversal, LocalDateTime.now());
    }

    @Override
    public Optional<CompletedService> findById(Long id) {
        return repository.findById(id);
    }

    //////
    @Override
    public List<ServicePeriodDto> countServicesByMonth(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.countServicesByMonth(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new ServicePeriodDto((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<RevenueByServiceTypeDto> getTotalRevenueByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getTotalRevenueByServiceType(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new RevenueByServiceTypeDto((String) result[0],
                        BigDecimal.valueOf((Double) result[1]).setScale(2, RoundingMode.HALF_EVEN)))
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentUtilizationDto> getEquipmentUtilization(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getEquipmentUtilization(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new EquipmentUtilizationDto(
                        (String) result[0],           // nome
                        (Long) result[1],             // quantidade de uso
                        ((BigDecimal) result[2]).setScale(2, RoundingMode.HALF_EVEN) // gasto total já como BigDecimal
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<EmployeeEfficiencyDto> getEmployeeEfficiency(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getEmployeeEfficiency(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new EmployeeEfficiencyDto(
                        (String) result[0],
                        (Long) result[1],
                        result[2] != null ? ((BigDecimal) result[2]).setScale(2, RoundingMode.HALF_EVEN) : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<LaborCostDto> getLaborCostByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getLaborCostByServiceType(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new LaborCostDto((String) result[0],
                        BigDecimal.valueOf((Double) result[1]).setScale(2, RoundingMode.HALF_EVEN)))
                .collect(Collectors.toList());
    }

    // Method for Equipment Cost by Service
    @Override
    public List<EquipmentCostDto> getEquipmentCostByServiceType(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getEquipmentCostByServiceType(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new EquipmentCostDto((String) result[0],
                        BigDecimal.valueOf((Double) result[1]).setScale(2, RoundingMode.HALF_EVEN)))
                .collect(Collectors.toList());
    }

    // Method for Labor Cost by Employee
    @Override
    public List<LaborCostDto> getLaborCostByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getLaborCostByEmployee(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new LaborCostDto((String) result[0],
                        BigDecimal.valueOf((Double) result[1]).setScale(2, RoundingMode.HALF_EVEN)))
                .collect(Collectors.toList());
    }

    // Method for Cost Revenue Comparison
    @Override
    public List<CostRevenueComparisonDto> getCostRevenueComparison(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Object[]> results = repository.getCostRevenueComparison(providerAccountId, startDateTime, endDateTime);
        return results.stream()
                .map(result -> new CostRevenueComparisonDto((String) result[0], BigDecimal.valueOf((Double) result[1]).setScale(2, RoundingMode.HALF_EVEN),
                        BigDecimal.valueOf((Double) result[2]).setScale(2, RoundingMode.HALF_EVEN),
                        BigDecimal.valueOf((Double) result[3]).setScale(2, RoundingMode.HALF_EVEN))).collect(Collectors.toList());
    }

    // Method for Inventory Efficiency
    @Override
    public List<EquipmentInByProviderAccountIdSumTotalDto> getInventoryEfficiency(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        log.info("Fetching equipment data for providerAccountId: {}, from: {}, to: {}", providerAccountId, startDateTime, endDateTime);
        List<Object[]> getEquipmentInById = repository.getEquipmentInByProviderAccountId(providerAccountId, startDateTime, endDateTime);

        List<EquipmentInByProviderAccountIdDto> equipmentList = getEquipmentInById.stream()
                .map(result -> new EquipmentInByProviderAccountIdDto((String) result[0],
                        result[1] != null ? ((Number) result[1]).longValue() : null,
                        result[2] != null ? ((Number) result[2]).longValue() : null))
                .collect(Collectors.toList());

        log.info("Fetched equipment data: {}", equipmentList);

        log.info("Fetching inventory efficiency data for providerAccountId: {}, from: {}, to: {}", providerAccountId, startDateTime, endDateTime);
        List<Object[]> results = repository.getInventoryEfficiency(providerAccountId, startDateTime, endDateTime);

        List<EquipmentInByProviderAccountIdSumTotalDto> collectSum = results.stream()
                .map(result -> {
                    String name = (String) result[0];
                    BigDecimal amount = ((BigDecimal) result[1]).setScale(2, RoundingMode.HALF_EVEN);
                    Long equipmentId = result[2] != null ? ((Number) result[2]).longValue() : null;


                    Optional<EquipmentInByProviderAccountIdDto> matchingDto = equipmentList.stream()
                            .filter(dto -> dto.getEquipmentId().equals(equipmentId))
                            .findFirst();

                    if (matchingDto.isPresent()) {
                        Long quantity = matchingDto.get().getQuantity();
                        BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_EVEN);
                        BigDecimal unitPrice = newAmount.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_EVEN);


                        log.info("Processed equipmentId: {}. Original amount: {}, Quantity: {}, New amount: {}",
                                equipmentId, amount, quantity, newAmount);

                        return new EquipmentInByProviderAccountIdSumTotalDto(name, newAmount, equipmentId, unitPrice, quantity);
                    } else {
                        log.info("No matching equipment found for equipmentId: {}", equipmentId);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("Calculated inventory efficiency data: {}", collectSum);

        return collectSum;
    }




    ////Relatório de Revisões e Manutenções
    @Override
    public MaintenanceRevisionsDto getMaintenanceAndRevisionsReport(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = repository.getMaintenanceAndRevisionsReport(providerAccountId, startDate, endDate);

        if (results.isEmpty()) {
            return new MaintenanceRevisionsDto(0L, 0L, 0L); // Returning default if no data found
        }

        Object[] result = results.get(0); // Assume only one row is returned
        Long scheduledRevisions = ((Number) result[0]).longValue();
        Long completedRevisions = ((Number) result[1]).longValue();
        Long canceledRevisions = ((Number) result[2]).longValue();

        return new MaintenanceRevisionsDto(scheduledRevisions, completedRevisions, canceledRevisions);
    }

}
