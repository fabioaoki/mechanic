package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderServiceIdentifier;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderServiceIdentifierException;
import br.com.mechanic.mechanic.mapper.ProviderServiceIdentifierMapper;
import br.com.mechanic.mechanic.repository.ProviderServiceIdentifierRepositoryImpl;
import br.com.mechanic.mechanic.response.ProviderServiceIdentifierResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Log4j2
@Service
public class ProviderServiceIdentifierService implements ProviderServiceIdentifierBO {

    private final ProviderServiceIdentifierRepositoryImpl providerServiceIdentifierRepository;
    @Override
    public Page<ProviderServiceIdentifierResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of provider service identifiers");
        return providerServiceIdentifierRepository.findAll(pageable).map(ProviderServiceIdentifierMapper.MAPPER::toDto);
    }

    @Override
    public ProviderServiceIdentifierResponseDto findById(Long id) {
        return ProviderServiceIdentifierMapper.MAPPER.toDto(getTypeService(id));
    }

    private ProviderServiceIdentifier getTypeService(Long id) {
        return providerServiceIdentifierRepository.findById(id).orElseThrow(() -> new ProviderServiceIdentifierException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "Provider identifier service not found by id: " + id));
    }

    @Override
    public ProviderServiceIdentifierResponseDto getTypeServiceByName(String identifier) {
        return ProviderServiceIdentifierMapper.MAPPER.toDto(findByIdentifier(identifier));
    }

    private ProviderServiceIdentifier findByIdentifier(String identifier) {
        return providerServiceIdentifierRepository.findByIdentifier(identifier).orElseThrow(() -> new ProviderServiceIdentifierException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "Provider identifier service not found by name: " + identifier));
    }
}