package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.service.request.PlateRequest;
import br.com.mechanic.mechanic.service.response.PlateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlateServiceBO {
    List<PlateResponseDto> save(final List<PlateRequest> plateRequests, final Long clientAccountId);

    Page<PlateResponseDto> findAll(final Pageable pageable);

    PlateResponseDto findById(final Long id);

    Page<PlateResponseDto> findAllByClientAccountId(final Long clientAccount, final Pageable pageable);

    PlateResponseDto updatePlate(final Long id, final PlateRequest requestDto);
}
