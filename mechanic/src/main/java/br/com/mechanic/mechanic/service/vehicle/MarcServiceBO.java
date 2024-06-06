package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.request.FindMarcAndYearRequest;
import br.com.mechanic.mechanic.request.MarcRequest;
import br.com.mechanic.mechanic.response.MarcResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MarcServiceBO {
    Page<MarcResponseDto> findAll(final Pageable pageable);

    MarcResponseDto findById(final Long id);

    Page<MarcResponseDto> findByNameAndYear(final FindMarcAndYearRequest request, final Pageable pageable);
}
