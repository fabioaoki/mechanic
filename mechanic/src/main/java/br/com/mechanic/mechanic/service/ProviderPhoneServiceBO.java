package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.response.ProviderAddressResponseDto;
import br.com.mechanic.mechanic.response.ProviderPhoneResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProviderPhoneServiceBO {
    void save(final List<ProviderPhoneRequest> phoneRequest, final Long personId, final Long providerAccountId);

    Page<ProviderPhoneResponseDto> findAll(final Pageable pageable);

    ProviderPhoneResponseDto findById(final Long id);

    Page<ProviderPhoneResponseDto> findAllByProviderAccountId(final Long providerAccountId, final Pageable pageable);

    ProviderPhoneResponseDto updateProviderPhone(final Long id, final ProviderPhoneRequest requestDto);
}
