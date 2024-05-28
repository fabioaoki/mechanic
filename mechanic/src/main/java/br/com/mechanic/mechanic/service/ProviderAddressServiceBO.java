package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ProviderAddressResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProviderAddressServiceBO {
    void save(List<ProviderAddressRequest> addressRequest, Long id);

    Page<ProviderAddressResponseDto> findAll(final Pageable pageable);

    ProviderAddressResponseDto findById(Long id);

    ProviderAddressResponseDto updateProviderAddress(Long id, ProviderAddressRequest requestDto);
}
