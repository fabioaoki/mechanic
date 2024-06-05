package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> color(String color);
}

