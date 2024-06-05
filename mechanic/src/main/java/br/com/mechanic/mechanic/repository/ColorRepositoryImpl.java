package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ColorRepositoryImpl {

    Page<Color> findAll(Pageable pageable);

    Optional<Color> findById(Long id);

    Color save(Color entity);

    Optional<Color> findByColor(String color);
}
