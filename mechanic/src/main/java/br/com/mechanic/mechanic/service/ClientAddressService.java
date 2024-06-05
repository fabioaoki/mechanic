package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ClientAddress;
import br.com.mechanic.mechanic.exception.ClientAddressException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountTypeException;
import br.com.mechanic.mechanic.mapper.ClientAddressMapper;
import br.com.mechanic.mechanic.model.ClientAddressModel;
import br.com.mechanic.mechanic.repository.ClientAddressRepositoryImpl;
import br.com.mechanic.mechanic.request.ClientAddressRequest;
import br.com.mechanic.mechanic.response.ClientAddressResponseDto;
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
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class ClientAddressService implements ClientAddressServiceBO {

    private final ClientAddressRepositoryImpl addressRepository;


    @Value("${google.api.geocoding.url}")
    private String geocodingUrl;

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    public ClientAddressService(ClientAddressRepositoryImpl addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public ClientAddressResponseDto save(ClientAddressRequest addressRequest, Long clientAccountId) {
        log.info("Service: valid address field");
        validAddressField(addressRequest);
        saveClientAddressRequest(addressRequest, clientAccountId);

        log.info("Service: saving a new client address");
        ClientAddress entity = ClientAddressMapper.MAPPER.toEntity(addressRequest);
        entity.setClientAccountId(clientAccountId);
//            try {
//                getGeocoding(addressRequest, entity);
//            } catch (ClientAddressException e) {
//                throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'address' field is invalid.");
//            }
        return ClientAddressMapper.MAPPER.toDto(addressRepository.save(entity));
    }

    @Override
    public Page<ClientAddressResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of providers address");
        return addressRepository.findAll(pageable).map(ClientAddressMapper.MAPPER::toDto);
    }

    @Override
    public ClientAddressResponseDto findById(Long id) {
        return ClientAddressMapper.MAPPER.toDto(getAddress(id));
    }

    @Override
    public ClientAddressResponseDto updateClientAddress(Long id, ClientAddressRequest requestDto) {
        log.info("Service update address by id: {}", id);
        ClientAddressModel addressModel = ClientAddressMapper.MAPPER.toModel(getAddress(id));
        boolean isChange = updateField(addressModel, requestDto);
        if (isChange) {
            findAddress(addressModel);
            ClientAddress clientAddress = addressRepository.save(ClientAddressMapper.MAPPER.modelToEntity(addressModel));
            return ClientAddressMapper.MAPPER.toDto(clientAddress);
        }
        throw new ClientAddressException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider address.");
    }

    @Override
    public Optional<ClientAddressResponseDto> findByProviderAccountId(Long clientAccount) {
        log.info("Retrieving of address by clientAccount");
        return addressRepository.findByClientAccountId(clientAccount)
                .map(ClientAddressMapper.MAPPER::toDto)
                .or(() -> {
                    log.error("Client address not found by clientAccountId: {}", clientAccount);
                    throw new ProviderAccountTypeException(ErrorCode.ERROR_CLIENT_ADDRESS_NOT_FOUND, "Client address not found by clientAccountId: " + clientAccount);
                });
    }

    private boolean updateField(ClientAddressModel addressModel, ClientAddressRequest requestDto) {
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

    private ClientAddress getAddress(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new ClientAddressException(ErrorCode.ERROR_CLIENT_ADDRESS_NOT_FOUND, "Client address not found by id: " + id));
    }

    private void validAddressField(ClientAddressRequest addressRequest) throws ClientAddressException {
        if (addressRequest.getCity() == null || addressRequest.getCity().trim().isEmpty()) {
            throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'city' field is required and cannot be empty.");
        }
        if (addressRequest.getStreet() == null || addressRequest.getStreet().trim().isEmpty()) {
            throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'street' field is required and cannot be empty.");
        }
        if (addressRequest.getZipCode() == null || addressRequest.getZipCode().trim().isEmpty()) {
            throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'zipcode' field is required and cannot be empty.");
        }
        if (addressRequest.getNeighborhood() == null || addressRequest.getNeighborhood().trim().isEmpty()) {
            throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'neighborhood' field is required and cannot be empty.");
        }
        if (addressRequest.getNumber() == null || addressRequest.getNumber().trim().isEmpty()) {
            throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'number' field is required and cannot be empty.");
        }
        if (addressRequest.getState() == null) {
            throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'state' field is required and cannot be empty.");
        }
        findAddress(ClientAddressMapper.MAPPER.requestToModel(addressRequest));
    }

    private void findAddress(ClientAddressModel addressRequest) {
        addressRepository.findByAddress(addressRequest.getCity(), addressRequest.getStreet(), addressRequest.getZipCode().replaceAll("\\s", ""),
                        addressRequest.getState().toString(), addressRequest.getNumber(), addressRequest.getNeighborhood())
                .ifPresent(address -> {
                    throw new ClientAddressException(ErrorCode.ERROR_CREATED_ADDRESS, "address already registered");
                });
    }

    private void saveClientAddressRequest(ClientAddressRequest addressRequests, Long clientAccountId) {
        log.info("Service: saving a new client address");
        ClientAddress entity = ClientAddressMapper.MAPPER.toEntity(addressRequests);
        entity.setClientAccountId(clientAccountId);
//            try {
//                getGeocoding(addressRequest, entity);
//            } catch (ClientAddressException e) {
//                throw new ClientAddressException(ErrorCode.INVALID_FIELD, "The 'address' field is invalid.");
//            }
        addressRepository.save(entity);
    }

    private void getGeocoding(ClientAddressRequest addressRequest, ClientAddress entity) {

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

    private String formatAddress(ClientAddressRequest addressRequest) {
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
