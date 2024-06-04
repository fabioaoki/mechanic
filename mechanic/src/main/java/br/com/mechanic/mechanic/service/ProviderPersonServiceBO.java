package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderPersonServiceBO {
    ProviderPersonResponseDto save(ProviderPersonRequest personRequest, Long id);

    Page<ProviderPersonResponseDto> findAll(final Pageable pageable);

    ProviderPersonResponseDto findById(Long id);

    ProviderPersonResponseDto updateProviderPerson(Long id, ProviderPersonUpdateRequest requestDto);
}
