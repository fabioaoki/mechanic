package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderPhoneRepository extends JpaRepository<ProviderPhone, Long> {}
