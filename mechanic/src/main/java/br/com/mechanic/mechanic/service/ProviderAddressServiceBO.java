package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.service.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.service.response.ProviderAddressResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProviderAddressServiceBO {
    void save(final List<ProviderAddressRequest> addressRequest, final Long id);

    Page<ProviderAddressResponseDto> findAll(final Pageable pageable);

    ProviderAddressResponseDto findById(final Long id);

    ProviderAddressResponseDto updateProviderAddress(final Long id, final ProviderAddressRequest requestDto);

    Page<ProviderAddressResponseDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);
}
