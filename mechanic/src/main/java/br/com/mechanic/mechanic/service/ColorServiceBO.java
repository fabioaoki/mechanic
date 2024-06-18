package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ColorRequest;
import br.com.mechanic.mechanic.response.ColorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ColorServiceBO {
    List<ColorResponseDto> save(ColorRequest colorRequest);

    Page<ColorResponseDto> findAll(final Pageable pageable);

    ColorResponseDto findById(Long id);

    ColorResponseDto findByColor(String color);
}
