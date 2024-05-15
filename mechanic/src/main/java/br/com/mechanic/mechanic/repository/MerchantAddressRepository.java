package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.MerchantAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantAddressRepository extends JpaRepository<MerchantAddress, Long> {}