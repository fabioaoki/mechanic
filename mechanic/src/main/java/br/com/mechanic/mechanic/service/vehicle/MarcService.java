package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Model;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ModelException;
import br.com.mechanic.mechanic.mapper.MarcMapper;
import br.com.mechanic.mechanic.repository.vehicle.ModelRepositoryImpl;
import br.com.mechanic.mechanic.response.MarcResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Log4j2
@Service
public class MarcService implements MarcServiceBO {

    private final ModelRepositoryImpl modelRepository;

    @Override
    public Page<MarcResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of model");
        return modelRepository.findAll(pageable).map(MarcMapper.MAPPER::toDto);
    }

    @Override
    public MarcResponseDto findById(Long id) {
        log.info("Finding plate by ID: {}", id);
        MarcResponseDto responseDto = MarcMapper.MAPPER.toDto(getModel(id));
        log.info("Marc found: {}", responseDto);
        return responseDto;
    }

    @Override
    public Page<MarcResponseDto> findByModelAndYear(String model, String year, Pageable pageable) {
        log.info("Retrieving list of model by model: {} and year: {}", model, year);
        Page<Model> models = modelRepository.findAllByModelAndYear(pageable, model, year);
        if (models.isEmpty()) {
            log.error("No models found by model: {} and year: {}", model, year);
            throw new ModelException(ErrorCode.MODEL_EXCEPTION, "No models found by model: " + model + " and year: " + year);
        }
        return models.map(MarcMapper.MAPPER::toDto);
    }

    @Override
    public Page<MarcResponseDto> findByMarcAndYear(String marc, String year, Pageable pageable) {
        log.info("Retrieving list of model by marc: {} and year: {}", marc, year);
        Page<Model> models = modelRepository.findAllMarcByMarcAndYear(pageable, marc, year);
        if (models.isEmpty()) {
            log.error("No models found by marc: {} and year: {}", marc, year);
            throw new ModelException(ErrorCode.MODEL_EXCEPTION, "No models found by marc: " + marc + " and year: " + year);
        }
        return models.map(MarcMapper.MAPPER::toDto);
    }

    private Model getModel(Long id) {
        log.info("Retrieving model by ID: {}", id);
        return modelRepository.findById(id).orElseThrow(() -> {
            log.error("Model not found by ID: {}", id);
            return new ModelException(ErrorCode.MODEL_EXCEPTION, "Model not found by id: " + id);
        });
    }

}