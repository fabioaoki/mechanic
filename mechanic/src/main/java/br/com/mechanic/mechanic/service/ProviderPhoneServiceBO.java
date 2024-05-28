package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ProviderPhoneRequest;

import java.util.List;

public interface ProviderPhoneServiceBO {
    void save(List<ProviderPhoneRequest> phoneRequest, Long personId, Long providerAccountId);
}
