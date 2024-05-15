package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderPackRepository extends JpaRepository<ProviderPack, Long> {}