package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ProviderAddressResponseDto;
import br.com.mechanic.mechanic.service.ProviderAddressServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/provider-address")
public class ProviderAddressController {

    @Autowired
    private ProviderAddressServiceBO addressServiceBO;

    @GetMapping
    public ResponseEntity<Page<ProviderAddressResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(addressServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderAddressById(@PathVariable Long id) {
        try {
            log.info("Fetching provider address with id: {}" , id);
            return ResponseEntity.ok(addressServiceBO.findById(id));
        } catch (ProviderAddressException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }
    @GetMapping("/provider-address/{provider-account-id}")
    public ResponseEntity<?> findAllByProviderAccountId(
            @PathVariable("provider-account-id") Long providerAccountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            return ResponseEntity.ok(addressServiceBO.findAllByProviderAccountId(providerAccountId, PageRequest.of(page, size)));
        } catch (ProviderAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProviderAddress(@PathVariable Long id,@RequestBody ProviderAddressRequest requestDto) {
        try {
            log.info("Updating provider address with id: {}" , id);
            return ResponseEntity.ok(addressServiceBO.updateProviderAddress(id, requestDto));
        } catch (ProviderAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderAddressException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
