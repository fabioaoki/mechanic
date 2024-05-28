package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.model.ProviderPersonResponseModel;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;

public interface ProviderPersonServiceBO {
    ProviderPersonResponseModel save(ProviderPersonRequest personRequest, Long id);
}
