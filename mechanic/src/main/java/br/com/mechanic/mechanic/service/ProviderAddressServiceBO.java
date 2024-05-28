package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ProviderAddressRequest;

import java.util.List;

public interface ProviderAddressServiceBO {
    void save(List<ProviderAddressRequest> addressRequest, Long id);
}
