package br.com.mechanic.mechanic.controller;

import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderServiceIdentifierException;
import br.com.mechanic.mechanic.response.ProviderServiceIdentifierResponseDto;
import br.com.mechanic.mechanic.service.ProviderServiceIdentifierBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/provider-service-identifier")
public class ProviderServiceIdentifierController {

    @Autowired
    private ProviderServiceIdentifierBO serviceIdentifierBO;

    @GetMapping
    public ResponseEntity<Page<ProviderServiceIdentifierResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(serviceIdentifierBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProvideServiceIdentifierById(@PathVariable Long id) {
        try {
            log.info("Fetching provider service identifier with id: " + id);
            return ResponseEntity.ok(serviceIdentifierBO.findById(id));
        } catch (ProviderServiceIdentifierException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/name/{identifier}")
    public ResponseEntity<?> getProviderServiceIdentifierByIdentifier(@PathVariable String identifier) {
        try {
            log.info("Fetching provider service identifier with identifier: " + identifier);
            return ResponseEntity.ok(serviceIdentifierBO.getTypeServiceByName(identifier));
        } catch (ProviderServiceIdentifierException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderServiceIdentifierException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}