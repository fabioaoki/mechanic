package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.pack.ProviderPack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderPackRepository extends JpaRepository<ProviderPack, Long> {}