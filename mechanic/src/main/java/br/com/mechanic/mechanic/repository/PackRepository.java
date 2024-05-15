package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {}
