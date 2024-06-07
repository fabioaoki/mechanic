package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.PlateException;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.mapper.PlateMapper;
import br.com.mechanic.mechanic.model.PlateModel;
import br.com.mechanic.mechanic.repository.vehicle.PlateRepositoryImpl;
import br.com.mechanic.mechanic.request.PlateRequest;
import br.com.mechanic.mechanic.response.PlateResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class PlateService implements PlateServiceBO {

    private final PlateRepositoryImpl plateRepository;
    private final CityUtil cityUtil;

    @Transactional
    @Override
    public List<PlateResponseDto> save(List<PlateRequest> plateRequests, Long clientAccountId) {
        List<PlateResponseDto> responseDtoList = new ArrayList<>();
        plateRequests.forEach(plateRequest -> {
            log.info("Service: valid plate field");
            validPlateField(plateRequest);
            log.info("Service: Saving a new plate");
            PlateModel plateModel = PlateMapper.MAPPER.dtoToModel(plateRequest);
            plateModel.setClientAccountId(clientAccountId);
            Plate plate = PlateMapper.MAPPER.modelToEntity(plateModel);
            PlateResponseDto responseDto = PlateMapper.MAPPER.toDto(plateRepository.save(plate));
            log.info("Service: Plate saved with ID: {}", responseDto.getId());
            responseDtoList.add(responseDto);
        });
        return responseDtoList;
    }

    @Override
    public Page<PlateResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of plates");
        return plateRepository.findAll(pageable).map(PlateMapper.MAPPER::toDto);
    }

    @Override
    public PlateResponseDto findById(Long id) {
        log.info("Finding plate by ID: {}", id);
        PlateResponseDto responseDto = PlateMapper.MAPPER.toDto(getPlate(id));
        log.info("Plate found: {}", responseDto);
        return responseDto;
    }

    @Override
    public Page<PlateResponseDto> findAllByClientAccountId(Long clientAccount, Pageable pageable) {
        log.info("Retrieving list of plates by clientAccountId: {}", clientAccount);
        return plateRepository.findAllByClientAccountId(pageable, clientAccount).map(PlateMapper.MAPPER::toDto);
    }

    public PlateResponseDto updatePlate(Long id, PlateRequest requestDto) {
        log.info("Service update plate by ID: {}", id);
        PlateModel plateModel = PlateMapper.MAPPER.toModel(getPlate(id));
        boolean isChange = updateField(plateModel, requestDto);
        if (isChange) {
            Plate plate = plateRepository.save(PlateMapper.MAPPER.modelToEntity(plateModel));
            log.info("Plate updated with ID: {}", plate.getId());
            return PlateMapper.MAPPER.toDto(plate);
        }
        log.warn("No changes were made to the plate with ID: {}", id);
        throw new ProviderAddressException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the vehicle type.");
    }

    public boolean updateField(PlateModel plateModel, PlateRequest requestDto) {
        boolean isChange = false;

        if ((Objects.nonNull(requestDto.getMercosulPlate()) && !requestDto.getMercosulPlate().isEmpty()) &&
                (Objects.nonNull(requestDto.getOldPlate()) && !requestDto.getOldPlate().isEmpty())) {
            log.error("Both mercosulPlate and oldPlate are filled.");
            throw new PlateException(ErrorCode.INVALID_FIELD, "Only one of 'mercosulPlate' or 'oldPlate' should be filled, not both.");
        }

        if (Objects.nonNull(requestDto.getMercosulPlate()) && !requestDto.getMercosulPlate().isEmpty()) {
            if (!requestDto.getMercosulPlate().matches("^[A-Z]{3}\\d[A-Z]\\d{2}$")) {
                log.error("Invalid mercosulPlate format: {}", requestDto.getMercosulPlate());
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'mercosulPlate' field is invalid. It should be in the format 'ABC1D23'.");
            }
            log.info("Validating if Mercosul plate exists: {}", requestDto.getMercosulPlate());
            mercosulPlateIsExists(requestDto.getMercosulPlate());
            if (!Objects.equals(plateModel.getMercosulPlate(), requestDto.getMercosulPlate())) {
                plateModel.setMercosulPlate(requestDto.getMercosulPlate());
                isChange = true;
                log.info("Mercosul plate updated to: {}", requestDto.getMercosulPlate());
            }
        } else if (Objects.nonNull(requestDto.getOldPlate()) && !requestDto.getOldPlate().isEmpty()) {
            if (!requestDto.getOldPlate().matches("^[A-Z]{3}-\\d{4}$")) {
                log.error("Invalid oldPlate format: {}", requestDto.getOldPlate());
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'oldPlate' field is invalid. It should be in the format 'AAA-1234'.");
            }
            if (requestDto.getCity() == null || requestDto.getCity().isEmpty()) {
                log.error("City is required when oldPlate is filled.");
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'city' field is required when 'oldPlate' is filled.");
            }
            log.info("Validating if old plate exists: {} in city: {}", requestDto.getOldPlate(), requestDto.getCity());
            oldPlateIsExists(requestDto.getOldPlate(), requestDto.getCity());
            if (!Objects.equals(plateModel.getOldPlate(), requestDto.getOldPlate())) {
                plateModel.setOldPlate(requestDto.getOldPlate());
                isChange = true;
                log.info("Old plate updated to: {}", requestDto.getOldPlate());
            }
        }

        return isChange;
    }

    private Plate getPlate(Long id) {
        log.info("Retrieving plate by ID: {}", id);
        return plateRepository.findById(id).orElseThrow(() -> {
            log.error("Plate not found by ID: {}", id);
            return new PlateException(ErrorCode.PLATE_EXCEPTION, "Plate not found by id: " + id);
        });
    }

    private void validPlateField(PlateRequest requestDto) {
        log.info("Validating plate fields.");

        if ((requestDto.getMercosulPlate() == null || requestDto.getMercosulPlate().isEmpty()) &&
                (requestDto.getOldPlate() == null || requestDto.getOldPlate().isEmpty())) {
            log.error("Neither mercosulPlate nor oldPlate is filled.");
            throw new PlateException(ErrorCode.INVALID_FIELD, "At least one of 'mercosulPlate' or 'oldPlate' must be filled.");
        }

        if ((requestDto.getMercosulPlate() != null && !requestDto.getMercosulPlate().isEmpty()) &&
                (requestDto.getOldPlate() != null && !requestDto.getOldPlate().isEmpty())) {
            log.error("Both mercosulPlate and oldPlate are filled.");
            throw new PlateException(ErrorCode.INVALID_FIELD, "Only one of 'mercosulPlate' or 'oldPlate' should be filled, not both.");
        }

        if (requestDto.getMercosulPlate() != null && !requestDto.getMercosulPlate().isEmpty()) {
            requestDto.setMercosulPlate(requestDto.getMercosulPlate().toUpperCase());
            if (!requestDto.getMercosulPlate().matches("^[A-Z]{3}\\d[A-Z]\\d{2}$")) {
                log.error("Invalid mercosulPlate format: {}", requestDto.getMercosulPlate());
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'mercosulPlate' field is invalid. It should be in the format 'ABC1D23'.");
            }
            log.info("Validating if Mercosul plate exists: {}", requestDto.getMercosulPlate());
            mercosulPlateIsExists(requestDto.getMercosulPlate());
            return;
        }

        if (requestDto.getOldPlate() != null && !requestDto.getOldPlate().isEmpty()) {
            requestDto.setOldPlate(requestDto.getOldPlate().toUpperCase());
            if (!requestDto.getOldPlate().matches("^[A-Z]{3}-\\d{4}$")) {
                log.error("Invalid oldPlate format: {}", requestDto.getOldPlate());
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'oldPlate' field is invalid. It should be in the format 'AAA-1234'.");
            }
            if (requestDto.getCity() == null || requestDto.getCity().isEmpty()) {
                log.error("City is required when oldPlate is filled.");
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'city' field is required when 'oldPlate' is filled.");
            }
            String formattedCity = cityUtil.formatCity(requestDto.getCity());
            if (!cityUtil.isValidCity(formattedCity)) {
                log.error("Invalid city: {}", formattedCity);
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'city' field is invalid.");
            }
            log.info("Validating if old plate exists: {} in city: {}", requestDto.getOldPlate(), formattedCity);
            oldPlateIsExists(requestDto.getOldPlate(), formattedCity);
        }
    }



    private void mercosulPlateIsExists(String mercosulPlate) {
        plateRepository.findByMercosulPlate(mercosulPlate)
                .ifPresent(number -> {
                    log.error("Mercosul plate already registered: {}", mercosulPlate);
                    throw new PlateException(ErrorCode.ERROR_CREATED_PLATE, "Mercosul plate already registered");
                });
    }

    private void oldPlateIsExists(String oldPlate, String city) {
        plateRepository.findByOldPlateAndCity(oldPlate, city)
                .ifPresent(number -> {
                    log.error("Old plate already registered in city: {} - {}", oldPlate, city);
                    throw new PlateException(ErrorCode.ERROR_CREATED_PLATE, "Old plate already registered in the specified city");
                });
    }
}