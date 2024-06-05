package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.Equipment;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.mapper.EquipmentMapper;
import br.com.mechanic.mechanic.model.EquipmentModel;
import br.com.mechanic.mechanic.repository.provider.EquipmentRepositoryImpl;
import br.com.mechanic.mechanic.request.EquipmentRequestDto;
import br.com.mechanic.mechanic.response.EquipmentResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class EquipmentService implements EquipmentServiceBO {

    private final EquipmentRepositoryImpl equipmentRepository;

    @Override
    public EquipmentResponseDto save(EquipmentRequestDto requestDto) {
        log.info("Service: valid equipment field");
        validTypeServiceField(requestDto);
        EquipmentModel equipmentModel = EquipmentMapper.MAPPER.requestToModel(requestDto);
        equipmentModel.setName(formatName(equipmentModel.getName().trim()));
        log.info("Service: Saving a new equipment ");
        Equipment equipment = EquipmentMapper.MAPPER.modelToEntity(equipmentModel);
        return EquipmentMapper.MAPPER.toDto(equipmentRepository.save(equipment));
    }

    public String formatName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    @Override
    public Page<EquipmentResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of equipments");
        return equipmentRepository.findAll(pageable).map(EquipmentMapper.MAPPER::toDto);
    }

    @Override
    public EquipmentResponseDto findById(Long id) {
        return EquipmentMapper.MAPPER.toDto(getTypeService(id));
    }

    @Override
    public EquipmentResponseDto updateEquipmentName(Long id, EquipmentRequestDto requestDto) throws EquipmentException {
        log.info("Service update equipment by id: {}", id);
        EquipmentModel equipmentModel = EquipmentMapper.MAPPER.toModel(getTypeService(id));
        boolean isChange = updateField(equipmentModel, requestDto);
        if (isChange) {
            equipmentModel.setName(formatName(equipmentModel.getName().trim()));
            equipmentIsExists(equipmentModel.getName());
            Equipment equipment = equipmentRepository.save(EquipmentMapper.MAPPER.modelToEntity(equipmentModel));
            return EquipmentMapper.MAPPER.toDto(equipment);
        }
        throw new EquipmentException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the equipment .");
    }

    private boolean updateField(EquipmentModel equipmentModel, EquipmentRequestDto requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getName()) && !Objects.equals(equipmentModel.getName(), requestDto.getName())) {
            equipmentModel.setName(requestDto.getName());
            isChange = true;
        }
        return isChange;
    }

    private Equipment getTypeService(Long id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new EquipmentException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "equipment not found by id: " + id));
    }

    private void validTypeServiceField(EquipmentRequestDto requestDto) {
        if (Objects.isNull(requestDto.getName()) || requestDto.getName().trim().isEmpty()) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'name' field is required and cannot be empty.");
        }
        equipmentIsExists(requestDto.getName());
    }

    private void equipmentIsExists(String name) {
        equipmentRepository.findByName(name)
                .ifPresent(equipment -> {
                    throw new EquipmentException(ErrorCode.ERROR_CREATED_TYPE_SERVICE, "Equipment already registered");
                });
    }
}