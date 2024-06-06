package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Marc;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.MarcException;
import br.com.mechanic.mechanic.mapper.MarcMapper;
import br.com.mechanic.mechanic.repository.vehicle.MarcRepositoryImpl;
import br.com.mechanic.mechanic.request.FindMarcAndYearRequest;
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

    private final MarcRepositoryImpl marcRepository;

    @Override
    public Page<MarcResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of marc");
        return marcRepository.findAll(pageable).map(MarcMapper.MAPPER::toDto);
    }

    @Override
    public MarcResponseDto findById(Long id) {
        log.info("Finding plate by ID: {}", id);
        MarcResponseDto responseDto = MarcMapper.MAPPER.toDto(getMarc(id));
        log.info("Marc found: {}", responseDto);
        return responseDto;
    }

    @Override
    public Page<MarcResponseDto> findByNameAndYear(FindMarcAndYearRequest request, final Pageable pageable) {
        log.info("Retrieving list of marc");
        return marcRepository.findAllByNameAndYear(pageable, request.getName(), request.getYear()).map(MarcMapper.MAPPER::toDto);
    }

    private Marc getMarc(Long id) {
        log.info("Retrieving marc by ID: {}", id);
        return marcRepository.findById(id).orElseThrow(() -> {
            log.error("Marc not found by ID: {}", id);
            return new MarcException(ErrorCode.MARC_EXCEPTION, "Marc not found by id: " + id);
        });
    }

}