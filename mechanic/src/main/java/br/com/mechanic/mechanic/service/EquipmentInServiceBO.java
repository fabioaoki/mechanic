package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.EquipmentInRequest;
import br.com.mechanic.mechanic.request.EquipmentInUpdateRequest;
import br.com.mechanic.mechanic.response.EquipmentInResponseDto;
import br.com.mechanic.mechanic.response.ProviderEquipmentInResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipmentInServiceBO {
    EquipmentInResponseDto save(final EquipmentInRequest equipmentInRequest);

    Page<EquipmentInResponseDto> findAll(final Pageable pageable);

    EquipmentInResponseDto findById(final Long id);

    Page<ProviderEquipmentInResponseDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    EquipmentInResponseDto updateEquipmentIn(final Long id, final EquipmentInUpdateRequest requestDto);
    void finish(final Long id);

    EquipmentInResponseDto findByProviderAccountAndEquipmentId(final Long providerAccountId, final Long equipmentId);

    EquipmentInResponseDto findByProviderAccountAndEquipmentIdAndFinishIsFalse(final Long providerAccountId, final Long equipmentId);
}
