package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.EquipmentOutRequest;
import br.com.mechanic.mechanic.request.EquipmentOutUpdateRequest;
import br.com.mechanic.mechanic.response.EquipmentOutResponseDto;
import br.com.mechanic.mechanic.response.EquipmentOutResponseDtoPage;
import br.com.mechanic.mechanic.response.EquipmentOutUpdateResponseDto;
import br.com.mechanic.mechanic.response.EquipmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface EquipmentOutServiceBO {
    EquipmentOutResponseDto save(final EquipmentOutRequest equipmentOutRequest);

    Page<EquipmentOutResponseDtoPage> findAll(final Pageable pageable);

    EquipmentOutResponseDto findById(final Long id);

    Page<EquipmentOutResponseDtoPage> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    EquipmentOutResponseDto updateEquipmentOut(final Long id, final EquipmentOutUpdateRequest requestDto);

    List<EquipmentOutResponseDto> findByProviderAccountAndEquipmentId(final Long providerAccountId, final Long id, final LocalDateTime createDate);

    void reversal(final Long id);

    List<EquipmentOutResponseDto> findAllByCompletedServiceId(final Long completedServiceId);
}
