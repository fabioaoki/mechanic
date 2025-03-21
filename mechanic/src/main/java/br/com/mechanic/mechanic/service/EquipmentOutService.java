package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.mapper.EquipmentOutMapper;
import br.com.mechanic.mechanic.repository.provider.EquipmentOutRepositoryImpl;
import br.com.mechanic.mechanic.service.model.EquipmentOutModel;
import br.com.mechanic.mechanic.service.request.EquipmentOutRequest;
import br.com.mechanic.mechanic.service.request.EquipmentOutUpdateRequest;
import br.com.mechanic.mechanic.service.response.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        EquipmentOut equipmentOut = equipmentOutRepository.save(EquipmentOutMapper.MAPPER.modelToEntity(EquipmentOutMapper.MAPPER.requestToModel(requestDto)));
        return EquipmentOutMapper.MAPPER.toDto(equipmentOut, equipmentResponseDto);
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
    public Page<EquipmentOutResponseDtoPage> findAll(Pageable pageable) {
        log.info("Retrieving list of equipments out");
        return equipmentOutRepository.findAll(pageable).map(EquipmentOutMapper.MAPPER::toDtoPage);
    }

    @Override
    public EquipmentOutResponseDto findById(Long id) {
        EquipmentOut equipment = getEquipmentById(id);
        EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findById(equipment.getEquipmentId());
        return EquipmentOutMapper.MAPPER.toDto(equipment, equipmentResponseDto);
    }

    @Override
    public Page<EquipmentOutResponseDtoPage> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of equipmentsIn by providerAccount");
        return equipmentOutRepository.findAllByProviderAccountId(pageable, providerAccountId).map(EquipmentOutMapper.MAPPER::toDtoPage);
    }

    @Override
    public EquipmentOutResponseDto updateEquipmentOut(Long id, EquipmentOutUpdateRequest requestDto) throws EquipmentException {
        log.info("Service update equipment by id: {}", id);
        EquipmentOutModel equipmentOutModel = EquipmentOutMapper.MAPPER.toModel(getEquipmentById(id));
        boolean isChange = updateField(equipmentOutModel, requestDto);
        if (isChange) {
            EquipmentOut equipmentOut = equipmentOutRepository.save(EquipmentOutMapper.MAPPER.modelToEntity(equipmentOutModel));
            EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findById(equipmentOut.getEquipmentId());
            return EquipmentOutMapper.MAPPER.toDto(equipmentOut, equipmentResponseDto);
        }
        throw new EquipmentException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the equipmentIn.");
    }

    @Override
    public List<EquipmentOutResponseDto> findByProviderAccountAndEquipmentId(Long providerAccountId, Long id, LocalDateTime createDate) {
        List<EquipmentOut> outList = equipmentOutRepository.findByProviderAccountAndEquipmentId(providerAccountId, id, createDate);
        return outList.stream()
                .map(EquipmentOutMapper.MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void reversal(Long id) {
        equipmentOutRepository.reversal(id);
    }

    @Override
    public List<EquipmentOutResponseDto> findAllByCompletedServiceId(Long completedServiceId) {
        log.info("get all completed service by id: {}", completedServiceId);
        List<EquipmentOut> outList = equipmentOutRepository.findAllByCompletedServiceId(completedServiceId);
        return outList.stream()
                .map(EquipmentOutMapper.MAPPER::toDto)
                .collect(Collectors.toList());
    }

    private boolean updateField(EquipmentOutModel equipmentInModel, EquipmentOutUpdateRequest outUpdateRequest) {
        boolean isChange = false;
        if (Objects.nonNull(outUpdateRequest.getEquipmentId()) && !Objects.equals(equipmentInModel.getEquipmentId(), outUpdateRequest.getEquipmentId())) {
            equipmentInModel.setEquipmentId(outUpdateRequest.getEquipmentId());
            isChange = true;
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
    }
}