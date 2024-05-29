package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProviderAddressRepositoryJpa implements ProviderAddressRepositoryImpl {

    private final ProviderAddressRepository addressRepository;

    @Autowired
    public ProviderAddressRepositoryJpa(ProviderAddressRepository providerAddressRepository) {
        this.addressRepository = providerAddressRepository;
    }

    @Override
    public Page<ProviderAddress> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @Override
    public Optional<ProviderAddress> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public Optional<ProviderAddress> findByAddress(String city, String street, String zipcode, String state, String number, String neighborhood) {
        return addressRepository.findByCityAndStreetAndZipCodeAndStateAndNumberAndNeighborhood(city, street, zipcode, state, number, neighborhood);
    }

    @Override
    public ProviderAddress save(ProviderAddress entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return addressRepository.save(entity);
    }

    @Override
    public Page<ProviderAddress> findByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return addressRepository.findAllByProviderAccountId(providerAccountId, pageable);
    }

}
