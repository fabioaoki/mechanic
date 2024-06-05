package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.ProviderAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderAccountTypeRepository extends JpaRepository<ProviderAccountType, Long> {
}
