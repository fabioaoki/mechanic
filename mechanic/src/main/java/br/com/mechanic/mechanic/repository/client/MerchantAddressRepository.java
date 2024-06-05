package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.provider.MerchantAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantAddressRepository extends JpaRepository<MerchantAddress, Long> {}