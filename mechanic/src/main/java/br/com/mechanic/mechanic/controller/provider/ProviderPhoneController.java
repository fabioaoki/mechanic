package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderPhoneException;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.service.ProviderPhoneServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/provider-phone")
public class ProviderPhoneController {

    @Autowired
    private ProviderPhoneServiceBO phoneServiceBO;

    @GetMapping
    public ResponseEntity<Page<?>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(phoneServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderPhoneById(@PathVariable Long id) {
        try {
            log.info("Fetching provider phone with id: " + id);
            return ResponseEntity.ok(phoneServiceBO.findById(id));
        } catch (ProviderPhoneException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }
    @GetMapping("/provider-phone/{provider-account-id}")
    public ResponseEntity<?> findAllByProviderAccountId(
            @PathVariable("provider-account-id") Long providerAccountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            return ResponseEntity.ok(phoneServiceBO.findAllByProviderAccountId(providerAccountId, PageRequest.of(page, size)));
        } catch (ProviderPhoneException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProviderAddress(@PathVariable Long id,@RequestBody ProviderPhoneRequest requestDto) {
        try {
            log.info("Updating provider phones with id: " + id);
            return ResponseEntity.ok(phoneServiceBO.updateProviderPhone(id, requestDto));
        } catch (ProviderPhoneException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderPhoneException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
