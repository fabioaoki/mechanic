package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.request.PlateRequest;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.response.PlateResponseDto;
import br.com.mechanic.mechanic.response.ProviderPhoneResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlateServiceBO {
    PlateResponseDto save(final PlateRequest phoneRequest, final Long clientAccountId);

    Page<PlateResponseDto> findAll(final Pageable pageable);

    PlateResponseDto findById(final Long id);

    Page<PlateResponseDto> findAllByClientAccountId(final Long clientAccount, final Pageable pageable);

    PlateResponseDto updateProviderPlate(final Long id, final PlateRequest requestDto);
}
