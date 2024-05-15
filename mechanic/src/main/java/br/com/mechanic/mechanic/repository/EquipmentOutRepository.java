package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EquipmentOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentOutRepository extends JpaRepository<EquipmentOut, Long> {}
