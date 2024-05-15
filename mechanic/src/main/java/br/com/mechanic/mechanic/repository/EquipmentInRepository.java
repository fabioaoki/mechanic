package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EquipmentIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentInRepository extends JpaRepository<EquipmentIn, Long> {}