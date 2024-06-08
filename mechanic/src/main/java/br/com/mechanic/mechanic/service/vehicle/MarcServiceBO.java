package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.response.MarcResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarcServiceBO {
    Page<MarcResponseDto> findAll(final Pageable pageable);

    MarcResponseDto findById(final Long id);

    Page<MarcResponseDto> findByModelAndYear(final String model, final String year, final Pageable pageable);

    Page<MarcResponseDto> findByMarcAndYear(final String marc, final String year, final Pageable pageable);
}
