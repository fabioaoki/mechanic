package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.response.ModelResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModelServiceBO {
    Page<ModelResponseDto> findAll(final Pageable pageable);

    ModelResponseDto findById(final Long id);

    Page<ModelResponseDto> findByModelAndYear(final String model, final String year, final Pageable pageable);

    Page<ModelResponseDto> findByMarcAndYear(final String marc, final String year, final Pageable pageable);
}
