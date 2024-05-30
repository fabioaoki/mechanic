package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.exception.TypeServiceException;
import br.com.mechanic.mechanic.request.TypeServiceRequestDto;
import br.com.mechanic.mechanic.response.TypeServiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TypeServiceBO {
    TypeServiceResponseDto findById(Long id);

    Page<TypeServiceResponseDto> findAll(final Pageable pageable);

    TypeServiceResponseDto save(TypeServiceRequestDto typeServiceRequestDto) throws TypeServiceException;

    TypeServiceResponseDto updateTypeServiceName(Long id, TypeServiceRequestDto requestDto) throws TypeServiceException;

}