package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.service.response.ProviderServiceIdentifierResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderServiceIdentifierBO {
    ProviderServiceIdentifierResponseDto findById(Long id);

    ProviderServiceIdentifierResponseDto getTypeServiceByName(String identifier);

    Page<ProviderServiceIdentifierResponseDto> findAll(final Pageable pageable);

}