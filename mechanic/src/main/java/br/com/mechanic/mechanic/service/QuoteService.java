package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.QuoteServiceEntity;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.QuoteServiceMapper;
import br.com.mechanic.mechanic.repository.provider.QuoteServiceRepositoryImpl;
import br.com.mechanic.mechanic.service.model.QuoteServiceModel;
import br.com.mechanic.mechanic.service.request.QuoteServiceRequest;
import br.com.mechanic.mechanic.service.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.service.response.QuoteServiceDescriptionResponseDto;
import br.com.mechanic.mechanic.service.response.QuoteServiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Log4j2
@Service
public class QuoteService implements QuoteServiceBO {

    private final QuoteServiceRepositoryImpl quoteServiceRepository;
    private final ProviderAccountServiceBO accountServiceBO;

    @Override
    public QuoteServiceResponseDto save(QuoteServiceRequest quoteServiceRequest, Long providerAccountId) {

        log.info("Starting to create quote service for provider account ID: {}", providerAccountId);
        validQuoteServiceField(quoteServiceRequest);

        ProviderAccountResponseDto providerAccount = accountServiceBO.findById(providerAccountId);
        log.info("Retrieved provider account status: {}", providerAccount.getStatus());
        if (providerAccount.getStatus().equals(ProviderAccountStatusEnum.CANCEL) || providerAccount.getStatus().equals(ProviderAccountStatusEnum.INITIAL_BLOCK)) {
            log.error("Provider account status is canceled");
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "The 'providerAccountStatus' is canceled or block.");
        }

        log.debug("Starting mapping of quote service request to model");
        QuoteServiceModel quoteServiceModel = QuoteServiceMapper.MAPPER.toModel(quoteServiceRequest);
        log.debug("Completed mapping of quote service request to model");
        List<Float> totalAmount = new ArrayList<>();

        AtomicReference<Long> quoteServiceId = new AtomicReference<>(null);

        quoteServiceModel.getDescriptions().forEach(description -> {
            QuoteServiceEntity entity = QuoteServiceMapper.MAPPER.modelToEntity(quoteServiceModel, providerAccountId, description);
            entity.setExpiredDate(LocalDateTime.now().plusMonths(1));

            if (quoteServiceId.get() != null) {
                entity.setQuoteServiceId(quoteServiceId.get());
            }

            QuoteServiceEntity save = quoteServiceRepository.save(entity);

            if ((save.getQuoteServiceId() != null && save.getQuoteServiceId() != 0L) && (quoteServiceId.get() == null)) {
                quoteServiceId.set(save.getQuoteServiceId());
            }

            totalAmount.add(save.getValue());
        });
        float sum = 0;
        for (Float value : totalAmount) {
            sum += (value == null ? 0 : value);
        }

        List<QuoteServiceDescriptionResponseDto> descriptionResponseDtoList = QuoteServiceMapper.MAPPER.toDescriptionDtoList(quoteServiceModel.getDescriptions());
        return QuoteServiceMapper.MAPPER.toResponseDto(quoteServiceId, providerAccount.getWorkshop(), quoteServiceModel, sum, LocalDate.now(), LocalDate.now().plusMonths(1), descriptionResponseDtoList);
    }

    private void validQuoteServiceField(QuoteServiceRequest quoteServiceRequest) {
        log.info("Service: valid quote field");
        if (StringUtils.isBlank(quoteServiceRequest.getClientName())) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'clientName' field is required and cannot be empty.");
        }

        if (CollectionUtils.isEmpty(quoteServiceRequest.getDescriptions())) {
            throw new QuoteServiceException(ErrorCode.INVALID_FIELD, "The 'descriptions' field is required and cannot be empty.");
        }

        quoteServiceRequest.getDescriptions().forEach(descriptionRequestDto -> {
            if (StringUtils.isBlank(descriptionRequestDto.getDescription())) {
                throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "Each 'description' in 'descriptions' is required and cannot be empty.");
            }
        });
    }
}