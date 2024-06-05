package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.Color;
import br.com.mechanic.mechanic.exception.ColorException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.mapper.ColorMapper;
import br.com.mechanic.mechanic.repository.ColorRepositoryImpl;
import br.com.mechanic.mechanic.request.ColorRequest;
import br.com.mechanic.mechanic.response.ColorResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Log4j2
@Service
public class ColorService implements ColorServiceBO {

    private final ColorRepositoryImpl colorRepository;

    @Transactional
    @Override
    public List<ColorResponseDto> save(ColorRequest colorRequest) {
        validColorField(colorRequest);
        List<ColorResponseDto> colors = new ArrayList<>();

        colorRequest.getColors().forEach(color -> {
            // Validar se essa cor é válida em português
            if (!ColorUtil.isValidColor(color)) {
                throw new ColorException(ErrorCode.INVALID_FIELD, "Invalid color value: " + color);
            }

            // Obter o valor hexadecimal da cor
            String colorHex = ColorUtil.getColorHex(color);

            // Validar se essa cor existe no repositório
            colorRepository.findByColor(colorHex).ifPresent(existingColor -> {
                throw new ColorException(ErrorCode.ERROR_CREATED_COLOR, "Color already registered");
            });

            // Salvar no repositório
            Color entity = colorRepository.save(ColorMapper.MAPPER.toEntity(colorHex));
            colors.add(ColorMapper.MAPPER.toDto(entity));
        });

        return colors;
    }

    @Override
    public Page<ColorResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of colors");
        return colorRepository.findAll(pageable)
                .map(color -> {
                    ColorResponseDto dto = ColorMapper.MAPPER.toDto(color);
                    String colorName = ColorUtil.getColorName(color.getColor());
                    dto.setColor(colorName);
                    return dto;
                });
    }

    @Override
    public ColorResponseDto findById(Long id) {
        Color color = getColorById(id);
        ColorResponseDto dto = ColorMapper.MAPPER.toDto(color);
        String colorName = ColorUtil.getColorName(color.getColor());
        dto.setColor(colorName);
        return dto;
    }

    private Color getColorById(Long id) {
        return colorRepository.findById(id).orElseThrow(() -> new ColorException(ErrorCode.ERROR_COLOR_NOT_FOUND, "Color not found by id: " + id));
    }

    private void validColorField(ColorRequest colorRequest) {
        if (colorRequest.getColors() == null || colorRequest.getColors().isEmpty() || colorRequest.getColors().stream().anyMatch(color -> color.trim().isEmpty())) {
            throw new ColorException(ErrorCode.INVALID_FIELD, "The 'colors' field is required.");
        }
    }
}
