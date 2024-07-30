package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.mapper.EquipmentInMapper;
import br.com.mechanic.mechanic.model.EquipmentInModel;
import br.com.mechanic.mechanic.repository.provider.EquipmentInRepositoryImpl;
import br.com.mechanic.mechanic.request.EquipmentInRequest;
import br.com.mechanic.mechanic.request.EquipmentInUpdateRequest;
import br.com.mechanic.mechanic.response.EquipmentInResponseDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderEquipmentInResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Log4j2
@Service
public class EquipmentInService implements EquipmentInServiceBO {

    private final EquipmentInRepositoryImpl equipmentInRepository;
    private final ProviderAccountServiceBO accountServiceBO;
    private final EquipmentServiceBO equipmentServiceBO;

    @Override
    public EquipmentInResponseDto save(EquipmentInRequest requestDto) {
        log.info("Service: Validating equipment field");
        validEquipmentInField(requestDto);

        EquipmentInModel equipmentModel = EquipmentInMapper.MAPPER.requestToModel(requestDto);
        Optional<EquipmentIn> equipmentInResponse = equipmentInRepository.findByProviderAccountIdAndEquipmentId(equipmentModel.getProviderAccountId(), equipmentModel.getEquipmentId());

        return equipmentInResponse.map(equipment -> {
            log.info("Service: Updating existing equipment");
            equipment.setQuantity(equipmentModel.getQuantity());
            equipment.setAmount(equipmentModel.getAmount().setScale(2, RoundingMode.HALF_UP));
            EquipmentIn updatedEquipment = equipmentInRepository.save(equipment);
            log.info("Service: Equipment updated with ID: {}", updatedEquipment.getId());
            return EquipmentInMapper.MAPPER.toDto(updatedEquipment);
        }).orElseGet(() -> {
            log.info("Service: Saving new equipment");
            EquipmentIn newEquipment = EquipmentInMapper.MAPPER.modelToEntity(equipmentModel);
            EquipmentIn savedEquipment = equipmentInRepository.save(newEquipment);
            log.info("Service: New equipment saved with ID: {}", savedEquipment.getId());
            return EquipmentInMapper.MAPPER.toDto(savedEquipment);
        });
    }


    @Override
    public Page<EquipmentInResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of equipments");
        return equipmentInRepository.findAll(pageable).map(EquipmentInMapper.MAPPER::toDto);
    }

    @Override
    public EquipmentInResponseDto findById(Long id) {
        return EquipmentInMapper.MAPPER.toDto(getEquipmentById(id));
    }

    @Override
    public Page<ProviderEquipmentInResponseDto> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of equipmentsIn by providerAccount");
        return equipmentInRepository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public EquipmentInResponseDto updateEquipmentIn(Long id, EquipmentInUpdateRequest requestDto) throws EquipmentException {
        log.info("Service update equipment by id: {}", id);
        EquipmentInModel equipmentInModel = EquipmentInMapper.MAPPER.toModel(getEquipmentById(id));
        boolean isChange = updateField(equipmentInModel, requestDto);
        if (isChange) {
            EquipmentIn equipmentIn = equipmentInRepository.save(EquipmentInMapper.MAPPER.modelToEntity(equipmentInModel));
            return EquipmentInMapper.MAPPER.toDto(equipmentIn);
        }
        throw new EquipmentException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the equipmentIn.");
    }

    @Override
    public void finish(Long id) {
        EquipmentInModel equipmentInModel = EquipmentInMapper.MAPPER.toModel(getEquipmentById(id));
        equipmentInModel.setFinish(true);
        equipmentInRepository.save(EquipmentInMapper.MAPPER.modelToEntity(equipmentInModel));
    }

    @Override
    public EquipmentInResponseDto findByProviderAccountAndEquipmentId(Long providerAccountId, Long equipmentId) {
        EquipmentIn equipmentIn = equipmentInRepository.findByProviderAccountIdAndEquipmentId(providerAccountId, equipmentId)
                .orElseThrow(() -> new EquipmentException(ErrorCode.ERROR_EQUIPMENT_NOT_FOUND, "Equipment not found by id: " + equipmentId));
        return EquipmentInMapper.MAPPER.toDto(equipmentIn);
    }

    @Override
    public EquipmentInResponseDto findByProviderAccountAndEquipmentIdAndFinishIsFalse(Long providerAccountId, Long equipmentId) {
        EquipmentIn equipmentIn = equipmentInRepository.findByProviderAccountIdAndEquipmentId(providerAccountId, equipmentId)
                .orElseThrow(() -> new EquipmentException(ErrorCode.ERROR_EQUIPMENT_NOT_FOUND, "Equipment not found by id: " + equipmentId));
        return EquipmentInMapper.MAPPER.toDto(equipmentIn);
    }

    private boolean updateField(EquipmentInModel equipmentInModel, EquipmentInUpdateRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getQuantity()) && !Objects.equals(equipmentInModel.getQuantity(), requestDto.getQuantity())) {
            equipmentInModel.setQuantity(requestDto.getQuantity());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getAmount()) && requestDto.getAmount().compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal currentAmount = equipmentInModel.getAmount();
            BigDecimal newAmount = requestDto.getAmount();
            if (currentAmount == null || currentAmount.compareTo(newAmount) != 0) {
                equipmentInModel.setAmount(newAmount);
                isChange = true;
            }
        }
        return isChange;
    }

    private EquipmentIn getEquipmentById(Long id) {
        return equipmentInRepository.findById(id).orElseThrow(() -> new EquipmentException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "equipmentIn not found by id: " + id));
    }

    private void validEquipmentInField(EquipmentInRequest requestDto) {
        if (Objects.isNull(requestDto.getEquipmentId()) || requestDto.getEquipmentId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'equipmentId' field is required and cannot be empty or zero.");
        }
        equipmentServiceBO.findById(requestDto.getEquipmentId());

        if (Objects.isNull(requestDto.getProviderAccountId()) || requestDto.getProviderAccountId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty or zero.");
        }
        ProviderAccountResponseDto accountResponseDto = accountServiceBO.findById(requestDto.getProviderAccountId());
        if (accountResponseDto.getStatus().equals(ProviderAccountStatusEnum.CANCEL))
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "Accounts already canceled");

        if (Objects.isNull(requestDto.getQuantity()) || requestDto.getQuantity() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'quantity' field is required and cannot be empty or zero.");
        }
        if (Objects.isNull(requestDto.getAmount()) || requestDto.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'amount' field is required and cannot be empty or zero.");
        }
    }
}