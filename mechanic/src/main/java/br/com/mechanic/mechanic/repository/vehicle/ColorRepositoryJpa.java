package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ColorRepositoryJpa implements ColorRepositoryImpl {

    private final ColorRepository repository;

    @Autowired
    public ColorRepositoryJpa(ColorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Color> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Color> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Color save(Color entity) {
        entity.setCreateDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public Optional<Color> findByColor(String color) {
        return repository.color(color);
    }
}