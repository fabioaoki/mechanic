package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.response.EmployeeServiceCountDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountCompletedServiceGroupByDateDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountGroupByDateDto;
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
    public Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId, LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;
            startDateTime = startDate.atTime(LocalTime.MIN);  // 00:00:00.000 do dia
            endDateTime = endDate.atTime(LocalTime.MAX);  // 23:59:59.999 do dia
            return repository.findByProviderAccountIdAndCreateDateBetween(providerAccountId, startDateTime, endDateTime, pageable);
        } else {
            return repository.findAllByProviderAccountId(pageable, providerAccountId);

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
    public Optional<CompletedService> findById(Long id) {
        return repository.findById(id);
    }

}
