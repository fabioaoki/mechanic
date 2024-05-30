package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.TypeService;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.TypeServiceException;
import br.com.mechanic.mechanic.mapper.TypeServiceMapper;
import br.com.mechanic.mechanic.model.TypeServiceModel;
import br.com.mechanic.mechanic.repository.TypeServiceRepositoryImpl;
import br.com.mechanic.mechanic.request.TypeServiceRequestDto;
import br.com.mechanic.mechanic.response.TypeServiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class TypeServiceManager implements TypeServiceBO {

    private final TypeServiceRepositoryImpl typeServiceRepository;
    @Override
    public TypeServiceResponseDto save(TypeServiceRequestDto requestDto) {
        log.info("Service: valid type service field");
        validTypeServiceField(requestDto);
        log.info("Service: Saving a new type service");
        TypeService vehicleType = TypeServiceMapper.MAPPER.toEntity(requestDto);
        return TypeServiceMapper.MAPPER.toDto(typeServiceRepository.save(vehicleType));
    }


    @Override
    public Page<TypeServiceResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of type services");
        return typeServiceRepository.findAll(pageable).map(TypeServiceMapper.MAPPER::toDto);
    }

    @Override
    public TypeServiceResponseDto findById(Long id) {
        return TypeServiceMapper.MAPPER.toDto(getTypeService(id));
    }

    @Override
    public TypeServiceResponseDto updateTypeServiceName(Long id, TypeServiceRequestDto requestDto) throws TypeServiceException {
        log.info("Service update type service by id: {}", id);
        TypeServiceModel vehicleTypeModel = TypeServiceMapper.MAPPER.toModel(getTypeService(id));
        boolean isChange = updateField(vehicleTypeModel, requestDto);
        if (isChange) {
            typeServiceIsExists(vehicleTypeModel.getName());
            TypeService vehicleType = typeServiceRepository.save(TypeServiceMapper.MAPPER.modelToEntity(vehicleTypeModel));
            return TypeServiceMapper.MAPPER.toDto(vehicleType);
        }
        throw new TypeServiceException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the type service.");
    }

    private boolean updateField(TypeServiceModel phoneModel, TypeServiceRequestDto requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getName()) && !Objects.equals(phoneModel.getName(), requestDto.getName())) {
            phoneModel.setName(requestDto.getName());
            isChange = true;
        }
        return isChange;
    }

    private TypeService getTypeService(Long id) {
        return typeServiceRepository.findById(id).orElseThrow(() -> new TypeServiceException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "type service not found by id: " + id));
    }

    private void validTypeServiceField(TypeServiceRequestDto requestDto) {
        if (Objects.isNull(requestDto.getName()) || requestDto.getName().trim().isEmpty()) {
            throw new TypeServiceException(ErrorCode.INVALID_FIELD, "The 'name' field is required and cannot be empty.");
        }
        typeServiceIsExists(requestDto.getName());
    }

    private void typeServiceIsExists(String name) {
        typeServiceRepository.findByName(name)
                .ifPresent(typeService -> {
                    throw new TypeServiceException(ErrorCode.ERROR_CREATED_TYPE_SERVICE, "Type service already registered");
                });
    }
}