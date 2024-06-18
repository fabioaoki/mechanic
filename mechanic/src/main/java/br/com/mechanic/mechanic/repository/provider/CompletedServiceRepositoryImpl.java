package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedServices;
import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompletedServiceRepositoryImpl {

    Page<CompletedServices> findAll(Pageable pageable);

    Optional<CompletedServices> findById(Long id);

    CompletedServices save(CompletedServices entity);

    Page<CompletedServices> findByProviderAccountId(Long providerAccountId);

    Page<CompletedServices> findByEmployeeAccountId(Long employeeAccountId);
    Page<CompletedServices> findByProviderServiceId(Long providerServiceId);
    Page<CompletedServices> findByPlateId(Long plateId);

}
