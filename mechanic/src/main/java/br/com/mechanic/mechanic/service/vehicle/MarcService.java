package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.MarcMapper;
import br.com.mechanic.mechanic.model.MarcModel;
import br.com.mechanic.mechanic.repository.vehicle.MarcRepositoryImpl;
import br.com.mechanic.mechanic.request.MarcRequest;
import br.com.mechanic.mechanic.request.PlateRequest;
import br.com.mechanic.mechanic.response.MarcResponseDto;
import br.com.mechanic.mechanic.response.PlateResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class MarcService implements MarcServiceBO {

    private final MarcRepositoryImpl marcRepository;

    @Transactional
    @Override
    public List<MarcResponseDto> save(List<MarcRequest> marcRequests) {
        List<MarcResponseDto> responseDtoList = new ArrayList<>();
        marcRequests.forEach(marcRequest -> {
            log.info("Service: valid marc field");
            validMarcField(marcRequest);
            log.info("Service: Saving a new marc");
            MarcModel marcModel = MarcMapper.MAPPER.dtoToModel(marcRequest);
            marcRepository.findByNameAndModelAndVersionAndYear(marcModel.getName(), marcModel.getModel(), marcRequest.getVersion(), marcModel.getYear());

            Plate plate = MarcMapper.MAPPER.toEntity(marcRequest);
            PlateResponseDto responseDto = MarcMapper.MAPPER.toDto(marcRepository.save(plate));
            log.info("Service: Plate saved with ID: {}", responseDto.getId());
            responseDtoList.add(responseDto);
        });
        return responseDtoList;
    }

    @Override
    public Page<PlateResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of plates");
        return marcRepository.findAll(pageable).map(MarcMapper.MAPPER::toDto);
    }

    @Override
    public PlateResponseDto findById(Long id) {
        log.info("Finding plate by ID: {}", id);
        PlateResponseDto responseDto = MarcMapper.MAPPER.toDto(getPlate(id));
        log.info("Plate found: {}", responseDto);
        return responseDto;
    }

    @Override
    public Page<PlateResponseDto> findAllByClientAccountId(Long clientAccount, Pageable pageable) {
        log.info("Retrieving list of plates by clientAccountId: {}", clientAccount);
        return marcRepository.findAllByClientAccountId(pageable, clientAccount).map(MarcMapper.MAPPER::toDto);
    }

    public PlateResponseDto updatePlate(Long id, PlateRequest requestDto) {
        log.info("Service update plate by ID: {}", id);
        MarcModel plateModel = MarcMapper.MAPPER.toModel(getPlate(id));
        boolean isChange = updateField(plateModel, requestDto);
        if (isChange) {
            Plate plate = marcRepository.save(MarcMapper.MAPPER.modelToEntity(plateModel));
            log.info("Plate updated with ID: {}", plate.getId());
            return MarcMapper.MAPPER.toDto(plate);
        }
        log.warn("No changes were made to the plate with ID: {}", id);
        throw new ProviderAddressException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the vehicle type.");
    }

    public boolean updateField(MarcModel plateModel, PlateRequest requestDto) {
        boolean isChange = false;

        if ((Objects.nonNull(requestDto.getMercosulPlate()) && !requestDto.getMercosulPlate().isEmpty()) &&
                (Objects.nonNull(requestDto.getOldPlate()) && !requestDto.getOldPlate().isEmpty())) {
            log.error("Both mercosulPlate and oldPlate are filled.");
            throw new MarcException(ErrorCode.INVALID_FIELD, "Only one of 'mercosulPlate' or 'oldPlate' should be filled, not both.");
        }

        if (Objects.nonNull(requestDto.getMercosulPlate()) && !requestDto.getMercosulPlate().isEmpty()) {
            if (!requestDto.getMercosulPlate().matches("^[A-Z]{3}\\d[A-Z]\\d{2}$")) {
                log.error("Invalid mercosulPlate format: {}", requestDto.getMercosulPlate());
                throw new MarcException(ErrorCode.INVALID_FIELD, "The 'mercosulPlate' field is invalid. It should be in the format 'ABC1D23'.");
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
                throw new MarcException(ErrorCode.INVALID_FIELD, "The 'oldPlate' field is invalid. It should be in the format 'AAA-1234'.");
            }
            if (requestDto.getCity() == null || requestDto.getCity().isEmpty()) {
                log.error("City is required when oldPlate is filled.");
                throw new MarcException(ErrorCode.INVALID_FIELD, "The 'city' field is required when 'oldPlate' is filled.");
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
        return marcRepository.findById(id).orElseThrow(() -> {
            log.error("Plate not found by ID: {}", id);
            return new MarcException(ErrorCode.PLATE_EXCEPTION, "Plate not found by id: " + id);
        });
    }

    private void validMarcField(MarcRequest requestDto) {
        log.info("Validating marc fields.");

        if (requestDto.getName() == null || requestDto.getName().isEmpty()) {
            throw new MarcException(ErrorCode.INVALID_FIELD, "The 'name' field is required and cannot be empty.");
        }
        if (requestDto.getModel() == null || requestDto.getModel().isEmpty()) {
            throw new MarcException(ErrorCode.INVALID_FIELD, "The 'model' field is required and cannot be empty.");
        }
        if (requestDto.getVersion() == null || requestDto.getVersion().isEmpty()) {
            throw new MarcException(ErrorCode.INVALID_FIELD, "The 'version' field is required and cannot be empty.");
        }
        if (requestDto.getYear() == null) {
            throw new MarcException(ErrorCode.INVALID_FIELD, "The 'name' field is required and cannot be empty.");
        }
        if (!isValidYear(requestDto.getYear())) {
            throw new MarcException(ErrorCode.INVALID_FIELD, "The 'year' field is invalid. It must be between 1950 and " + (Year.now().getValue() + 1) + ".");
        }
    }
    public boolean isValidYear(LocalDate year) {
        int currentYear = Year.now().getValue();
        int minYear = 1950;
        int maxYear = currentYear + 1;
        int yearValue = year.getYear();
        return yearValue >= minYear && yearValue <= maxYear;
    }

    private void mercosulPlateIsExists(String mercosulPlate) {
        marcRepository.findByMercosulPlate(mercosulPlate)
                .ifPresent(number -> {
                    log.error("Mercosul plate already registered: {}", mercosulPlate);
                    throw new MarcException(ErrorCode.ERROR_CREATED_PLATE, "Mercosul plate already registered");
                });
    }

    private void oldPlateIsExists(String oldPlate, String city) {
        marcRepository.findByOldPlateAndCity(oldPlate, city)
                .ifPresent(number -> {
                    log.error("Old plate already registered in city: {} - {}", oldPlate, city);
                    throw new MarcException(ErrorCode.ERROR_CREATED_PLATE, "Old plate already registered in the specified city");
                });
    }
}