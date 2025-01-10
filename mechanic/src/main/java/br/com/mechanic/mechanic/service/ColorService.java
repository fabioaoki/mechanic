package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.vehicle.Color;
import br.com.mechanic.mechanic.exception.ColorException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.mapper.ColorMapper;
import br.com.mechanic.mechanic.repository.vehicle.ColorRepositoryImpl;
import br.com.mechanic.mechanic.service.request.ColorRequest;
import br.com.mechanic.mechanic.service.response.ColorResponseDto;
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
            log.info("Processing color: {}", color);

            // Validar se essa cor é válida em português
            if (!ColorUtil.isValidColor(color)) {
                log.error("Invalid color value: {}", color);
                throw new ColorException(ErrorCode.INVALID_FIELD, "Invalid color value: " + color);
            }

            // Obter o nome da cor formatado
            String formattedColor = ColorUtil.formatColor(color);

            // Validar se essa cor existe no repositório
            validColorName(formattedColor);

            // Salvar no repositório
            Color entity = colorRepository.save(ColorMapper.MAPPER.toEntity(formattedColor));
            log.info("Color saved: {}", formattedColor);

            colors.add(ColorMapper.MAPPER.toDto(entity));
        });

        log.info("All colors processed and saved");
        return colors;
    }

    private void validColorName(String formattedColor) {
        colorRepository.findByColor(formattedColor).ifPresent(existingColor -> {
            log.error("Color already registered: {}", formattedColor);
            throw new ColorException(ErrorCode.ERROR_CREATED_COLOR, "Color already registered");
        });
    }

    @Override
    public Page<ColorResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of colors");
        return colorRepository.findAll(pageable)
                .map(color -> {
                    ColorResponseDto dto = ColorMapper.MAPPER.toDto(color);
                    String colorName = ColorUtil.formatColor(color.getColor());
                    dto.setColor(colorName);
                    log.info("Color retrieved: {}", colorName);
                    return dto;
                });
    }

    @Override
    public ColorResponseDto findById(Long id) {
        log.info("Retrieving color by id: {}", id);
        Color color = getColorById(id);
        ColorResponseDto dto = ColorMapper.MAPPER.toDto(color);
        String colorName = ColorUtil.formatColor(color.getColor());
        dto.setColor(colorName);
        log.info("Color retrieved by id: {} - {}", id, colorName);
        return dto;
    }

    @Override
    public ColorResponseDto findByColor(String color) {
        String formattedColor = ColorUtil.formatColor(color);
        return ColorMapper.MAPPER.toDto(colorRepository.findByColor(formattedColor).orElseThrow(() -> {
            log.error("Color not found by name: {}", formattedColor);
            return new ColorException(ErrorCode.ERROR_COLOR_NOT_FOUND, "Color not found by id: " + formattedColor);
        }));
    }

    private Color getColorById(Long id) {
        log.info("Searching for color by id: {}", id);
        return colorRepository.findById(id).orElseThrow(() -> {
            log.error("Color not found by id: {}", id);
            return new ColorException(ErrorCode.ERROR_COLOR_NOT_FOUND, "Color not found by id: " + id);
        });
    }

    private void validColorField(ColorRequest colorRequest) {
        log.info("Validating color request");
        if (colorRequest.getColors() == null || colorRequest.getColors().isEmpty() || colorRequest.getColors().stream().anyMatch(color -> color.trim().isEmpty())) {
            log.error("The 'colors' field is required and must not contain empty values");
            throw new ColorException(ErrorCode.INVALID_FIELD, "The 'colors' field is required.");
        }
    }
}
