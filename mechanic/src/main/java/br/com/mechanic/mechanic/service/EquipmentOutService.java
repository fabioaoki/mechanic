package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.EquipmentIn;
import br.com.mechanic.mechanic.entity.EquipmentOut;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.mapper.EquipmentInMapper;
import br.com.mechanic.mechanic.mapper.EquipmentOutMapper;
import br.com.mechanic.mechanic.model.EquipmentInModel;
import br.com.mechanic.mechanic.repository.EquipmentOutRepositoryImpl;
import br.com.mechanic.mechanic.request.EquipmentInUpdateRequest;
import br.com.mechanic.mechanic.request.EquipmentOutRequest;
import br.com.mechanic.mechanic.response.EquipmentInResponseDto;
import br.com.mechanic.mechanic.response.EquipmentOutResponseDto;
import br.com.mechanic.mechanic.response.EquipmentResponseDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class EquipmentOutService implements EquipmentOutServiceBO {

    private final EquipmentOutRepositoryImpl equipmentOutRepository;
    private final ProviderAccountServiceBO accountServiceBO;
    private final EquipmentServiceBO equipmentServiceBO;
    private final EquipmentInServiceBO equipmentInServiceBO;

    @Override
    public EquipmentOutResponseDto save(EquipmentOutRequest requestDto) {
        log.info("Service: Validating equipment out field");
        validEquipmentOutField(requestDto);
        EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findById(requestDto.getEquipmentId());

        haveEquipment(requestDto);
        //TRAZER FUNCIONARIO
        //COMPLETAR UM SERVIÇO


        //GRAVAR UMA TRANSAÇÃO
        EquipmentOut equipmentOut = equipmentOutRepository.save(EquipmentOutMapper.MAPPER.modelToEntity(EquipmentOutMapper.MAPPER.requestToModel(requestDto)));


        return ;
    }

    private void haveEquipment(EquipmentOutRequest requestDto) {
        Long outEquipment = equipmentOutRepository.countByProviderAccountIdAndEquipmentId(requestDto.getProviderAccountId(), requestDto.getProviderAccountId());
        EquipmentInResponseDto equipmentIn = equipmentInServiceBO.findByProviderAccountAndEquipmentId(requestDto.getProviderAccountId(), requestDto.getEquipmentId());
        long remainingQuantity = equipmentIn.getQuantity() - outEquipment;
        if (remainingQuantity <= 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The quantity minus outEquipment must be greater than zero.");
        }
    }


    @Override
    public Page<EquipmentOutResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of equipments out");
        return equipmentOutRepository.findAll(pageable).map(EquipmentOutMapper.MAPPER::toDto);
    }

    @Override
    public EquipmentOutResponseDto findById(Long id) {
        return EquipmentOutMapper.MAPPER.toDto(getEquipmentById(id));
    }

    @Override
    public Page<EquipmentInResponseDto> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of equipmentsIn by providerAccount");
        return equipmentOutRepository.findAllByProviderAccountId(pageable, providerAccountId).map(EquipmentInMapper.MAPPER::toDto);
    }

    @Override
    public EquipmentInResponseDto updateEquipmentOut(Long id, EquipmentInUpdateRequest requestDto) throws EquipmentException {
        log.info("Service update equipment by id: {}", id);
        EquipmentInModel equipmentInModel = EquipmentInMapper.MAPPER.toModel(getEquipmentById(id));
        boolean isChange = updateField(equipmentInModel, requestDto);
        if (isChange) {
            EquipmentIn equipmentIn = equipmentOutRepository.save(EquipmentInMapper.MAPPER.modelToEntity(equipmentInModel));
            return EquipmentInMapper.MAPPER.toDto(equipmentIn);
        }
        throw new EquipmentException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the equipmentIn.");
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

    private EquipmentOut getEquipmentById(Long id) {
        return equipmentOutRepository.findById(id).orElseThrow(() -> new EquipmentException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "equipmentOut not found by id: " + id));
    }

    private void validEquipmentOutField(EquipmentOutRequest requestDto) {
        if (Objects.isNull(requestDto.getEquipmentId()) || requestDto.getEquipmentId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'equipmentId' field is required and cannot be empty or zero.");
        }

        if (Objects.isNull(requestDto.getProviderAccountId()) || requestDto.getProviderAccountId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty or zero.");
        }
        ProviderAccountResponseDto accountResponseDto = accountServiceBO.findById(requestDto.getProviderAccountId());
        if (accountResponseDto.getStatus().equals(ProviderAccountStatusEnum.CANCEL))
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "Accounts already canceled");

        if (Objects.isNull(requestDto.getAmount()) || requestDto.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'amount' field is required and cannot be empty or zero.");
        }
    }
}