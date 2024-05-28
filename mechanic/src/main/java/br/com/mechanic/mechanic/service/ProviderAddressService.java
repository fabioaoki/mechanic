package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.mapper.ProviderAddressMapper;
import br.com.mechanic.mechanic.repository.ProviderAddressRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@AllArgsConstructor
@Log4j2
@Service
public class ProviderAddressService implements ProviderAddressServiceBO {


    @Value("${google.api.geocoding.url}")
    private String geocodingUrl;

    @Value("${google.api.key}")
    private String apiKey;

    private final ProviderAddressRepositoryImpl addressRepository;


    @Override
    public void save(List<ProviderAddressRequest> addressList, Long providerAccountId) {
        log.info("Service: valid address field");
        validAddressField(addressList);
        saveProviderAddressRequest(addressList, providerAccountId);

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
            if (addressRequest.getState() == null || addressRequest.getState().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'state' field is required and cannot be empty.");
            }
            addressRepository.findByAddress(addressRequest.getCity(), addressRequest.getStreet(), addressRequest.getZipCode().replaceAll("\\s", ""),
                            addressRequest.getState(), addressRequest.getNumber(), addressRequest.getNeighborhood())
                    .ifPresent(address -> {
                        throw new ProviderAddressException(ErrorCode.ERROR_CREATED_ADDRESS, "address already registered");
                    });
        });

    }
    private void saveProviderAddressRequest(List<ProviderAddressRequest> addressRequestList, Long providerAccountId) {
        log.info("Service: saving a new provider address");
        addressRequestList.forEach(addressRequest -> {
            ProviderAddress entity = ProviderAddressMapper.MAPPER.toEntity(addressRequest);
            entity.setProviderAccountId(providerAccountId);
            try {
                getGeocoding(addressRequest, entity);
            } catch (ProviderAddressException e) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'address' field is invalid.");
            }
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
