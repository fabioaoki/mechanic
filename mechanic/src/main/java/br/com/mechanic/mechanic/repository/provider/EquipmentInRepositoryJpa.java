package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import br.com.mechanic.mechanic.response.EquipmentInResponseDto;
import br.com.mechanic.mechanic.response.ProviderEquipmentInResponseDto;
import br.com.mechanic.mechanic.response.ProviderServiceCountGroupByDateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EquipmentInRepositoryJpa implements EquipmentInRepositoryImpl {

    EquipmentInRepository repository;

    public EquipmentInRepositoryJpa(EquipmentInRepository repository) {
        this.repository = repository;
    }

    @Override
    public EquipmentIn save(EquipmentIn entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<EquipmentIn> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ProviderEquipmentInResponseDto> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        Page<Object[]> results =  repository.findAllByProviderAccountId(pageable, providerAccountId);

        List<ProviderEquipmentInResponseDto> dtos = results.getContent().stream()
                .map(result -> new ProviderEquipmentInResponseDto(
                        result[0] != null ? ((Number) result[0]).longValue() : null, // Conversão segura para Long
                        result[1] != null ? ((Number) result[1]).longValue() : null, // Conversão segura para Long
                        (String) result[2], // Nome como String
                        result[7] != null ? new BigDecimal(result[7].toString()) : null, // Conversão segura para BigDecimal
                        result[6] != null ? ((Number) result[6]).longValue() : null, // Conversão segura para Long
                        result[5] != null && (boolean) result[5], // Boolean para 'finish'
                        result[3] != null ? ((Timestamp) result[3]).toLocalDateTime() : null, // Conversão segura de Timestamp para LocalDateTime
                        result[4] != null ? ((Timestamp) result[4]).toLocalDateTime() : null  // Conversão segura de Timestamp para LocalDateTime
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, results.getTotalElements());
    }

    @Override
    public Optional<EquipmentIn> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<EquipmentIn> findByProviderAccountIdAndEquipmentId(Long providerAccountId, Long equipmentId) {
        return repository.findByProviderAccountIdAndEquipmentIdAndFinishIsFalse(providerAccountId, equipmentId);
    }

    @Override
    public Optional<EquipmentIn> findByProviderAccountId(Long providerAccountId) {
        return repository.findByProviderAccountId(providerAccountId);
    }
}