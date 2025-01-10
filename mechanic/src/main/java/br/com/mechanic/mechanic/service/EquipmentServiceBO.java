package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.service.request.EquipmentRequestDto;
import br.com.mechanic.mechanic.service.response.EquipmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipmentServiceBO {
    EquipmentResponseDto findById(Long id);

    Page<EquipmentResponseDto> findAll(final Pageable pageable);

    EquipmentResponseDto save(EquipmentRequestDto typeServiceRequestDto) throws EquipmentException;

    EquipmentResponseDto updateEquipmentName(Long id, EquipmentRequestDto requestDto) throws EquipmentException;

    EquipmentResponseDto findByProviderServiceIdentifierId(Long providerServiceIdentifierId);
}