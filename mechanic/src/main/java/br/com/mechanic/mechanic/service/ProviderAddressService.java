package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.ProviderAddress;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.mapper.ProviderAddressMapper;
import br.com.mechanic.mechanic.model.ProviderAddressModel;
import br.com.mechanic.mechanic.repository.provider.ProviderAddressRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.response.ProviderAddressResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@AllArgsConstructor
public class ProviderAddressService implements ProviderAddressServiceBO {

    private final ProviderAddressRepositoryImpl addressRepository;


    @Value("${google.api.geocoding.url}")
    private String geocodingUrl;

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    public ProviderAddressService(ProviderAddressRepositoryImpl addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void save(List<ProviderAddressRequest> addressList, Long providerAccountId) {
        log.info("Service: valid address field");
        validAddressField(addressList);
        saveProviderAddressRequest(addressList, providerAccountId);

    }

    @Override
    public Page<ProviderAddressResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of providers address");
        return addressRepository.findAll(pageable).map(ProviderAddressMapper.MAPPER::toDto);
    }

    @Override
    public ProviderAddressResponseDto findById(Long id) {
        return ProviderAddressMapper.MAPPER.toDto(getAddress(id));
    }

    @Override
    public ProviderAddressResponseDto updateProviderAddress(Long id, ProviderAddressRequest requestDto) {
        log.info("Service update address by id: {}", id);
        ProviderAddressModel addressModel = ProviderAddressMapper.MAPPER.toModel(getAddress(id));
        boolean isChange = updateField(addressModel, requestDto);
        if (isChange) {
            findAddress(addressModel);
            ProviderAddress providerAddress = addressRepository.save(ProviderAddressMapper.MAPPER.modelToEntity(addressModel));
            return ProviderAddressMapper.MAPPER.toDto(providerAddress);
        }
        throw new ProviderAddressException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider address.");
    }

    @Override
    public Page<ProviderAddressResponseDto> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of address by provider");
        Page<ProviderAddress> addresses = addressRepository.findByProviderAccountId(pageable, providerAccountId);

        if (addresses.isEmpty()) {
            throw new ProviderAddressException(ErrorCode.ERROR_PROVIDER_ACCOUNT_NOT_FOUND, "Provider account not found by id: " + providerAccountId);
        }

        return addresses.map(ProviderAddressMapper.MAPPER::toDto);
    }

    private boolean updateField(ProviderAddressModel addressModel, ProviderAddressRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getStreet()) && !Objects.equals(addressModel.getStreet(), requestDto.getStreet())) {
            addressModel.setStreet(requestDto.getStreet().trim());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getNumber()) && !Objects.equals(addressModel.getNumber(), requestDto.getNumber())) {
            addressModel.setNumber(requestDto.getNumber().trim());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getNeighborhood()) && !Objects.equals(addressModel.getNeighborhood(), requestDto.getNeighborhood())) {
            addressModel.setNeighborhood(requestDto.getNeighborhood().trim());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getZipCode()) && !Objects.equals(addressModel.getZipCode(), requestDto.getZipCode())) {
            addressModel.setZipCode(requestDto.getZipCode().trim());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getCity()) && !Objects.equals(addressModel.getCity(), requestDto.getCity())) {
            addressModel.setCity(requestDto.getCity().trim());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getState()) && !Objects.equals(addressModel.getState(), requestDto.getState())) {
            addressModel.setState(requestDto.getState());
            isChange = true;
        }
        return isChange;
    }

    private ProviderAddress getAddress(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new ProviderAddressException(ErrorCode.ERROR_PROVIDER_ADDRESS_NOT_FOUND, "Provider address not found by id: " + id));
    }

    private void validAddressField(List<ProviderAddressRequest> addressList) throws ProviderAddressException {
        addressList.forEach(addressRequest -> {
            if (addressRequest.getCity() == null || addressRequest.getCity().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'city' field is required and cannot be empty.");
            }
            if (addressRequest.getStreet() == null || addressRequest.getStreet().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'street' field is required and cannot be empty.");
            }
            if (addressRequest.getZipCode() == null || addressRequest.getZipCode().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'zipcode' field is required and cannot be empty.");
            }
            if (addressRequest.getNeighborhood() == null || addressRequest.getNeighborhood().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'neighborhood' field is required and cannot be empty.");
            }
            if (addressRequest.getNumber() == null || addressRequest.getNumber().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'number' field is required and cannot be empty.");
            }
            if (addressRequest.getState() == null) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'state' field is required and cannot be empty.");
            }
            findAddress(ProviderAddressMapper.MAPPER.requestToModel(addressRequest));
        });

    }

    private void findAddress(ProviderAddressModel addressRequest) {
        addressRepository.findByAddress(addressRequest.getCity(), addressRequest.getStreet(), addressRequest.getZipCode().replaceAll("\\s", ""),
                        addressRequest.getState().toString(), addressRequest.getNumber(), addressRequest.getNeighborhood())
                .ifPresent(address -> {
                    throw new ProviderAddressException(ErrorCode.ERROR_CREATED_ADDRESS, "address already registered");
                });
    }

    private void saveProviderAddressRequest(List<ProviderAddressRequest> addressRequestList, Long providerAccountId) {
        log.info("Service: saving a new provider address");
        addressRequestList.forEach(addressRequest -> {
            ProviderAddress entity = ProviderAddressMapper.MAPPER.toEntity(addressRequest);
            entity.setProviderAccountId(providerAccountId);
//            try {
//                getGeocoding(addressRequest, entity);
//            } catch (ProviderAddressException e) {
//                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'address' field is invalid.");
//            }
            addressRepository.save(entity);
        });
    }

    private void getGeocoding(ProviderAddressRequest addressRequest, ProviderAddress entity) {

        String address = formatAddress(addressRequest);
        String url = geocodingUrl + "?address=" + address + "&key=" + apiKey;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject location = jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                entity.setLatitude(location.getDouble("lat"));
                entity.setLongitude(location.getDouble("lng"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatAddress(ProviderAddressRequest addressRequest) {
        try {
            String address = addressRequest.getStreet() + " " +
                    addressRequest.getNumber() + " " +
                    addressRequest.getNeighborhood() + " " +
                    addressRequest.getZipCode() + " " +
                    addressRequest.getCity() + " " +
                    addressRequest.getState();
            return URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode address", e);
            return "";
        }
    }
}
