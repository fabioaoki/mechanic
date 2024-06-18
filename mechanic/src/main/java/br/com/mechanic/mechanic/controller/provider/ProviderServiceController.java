package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.request.ProviderServiceUpdateRequestDto;
import br.com.mechanic.mechanic.response.ProviderServiceResponseDto;
import br.com.mechanic.mechanic.service.ProviderServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/provider-service")
public class ProviderServiceController {

    @Autowired
    private ProviderServiceBO providerServiceBO;

    @GetMapping
    public ResponseEntity<Page<ProviderServiceResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(providerServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderServiceById(@PathVariable Long id) {
        try {
            log.info("Fetching provider service with id: {}" , id);
            return ResponseEntity.ok(providerServiceBO.findById(id));
        } catch (ProviderServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProviderService(@RequestBody ProviderServiceRequest providerServiceRequest) throws ProviderServiceException, VehicleTypeException, ProviderAccountException {
        try {
            log.info("Creating a provider service");
            return ResponseEntity.ok(providerServiceBO.save(providerServiceRequest));
        } catch (ProviderServiceException e) {
            log.error("ProviderServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (VehicleTypeException e) {
            log.error("VehicleTypeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            log.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (EquipmentException e) {
            log.error("TypeServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderServiceIdentifierException e) {
            log.error("ProviderServiceIdentifierException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProviderService(@PathVariable Long id) throws ProviderAccountException {
        try {
            log.info("Deleting provider service with id: {}" , id);
            providerServiceBO.isUsed(id, false);
            return ResponseEntity.ok().build();
        } catch (ProviderServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("active/{id}")
    public ResponseEntity<?> activeProviderService(@PathVariable Long id) {
        try {
            log.info("Activating provider account with id: {}" , id);
            providerServiceBO.isUsed(id, true);
            return ResponseEntity.ok().build();
        } catch (ProviderServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProviderService(@PathVariable Long id, @RequestBody ProviderServiceUpdateRequestDto requestDto) {
        try {
            log.info("Updating provider account with id: {}" , id);
            return ResponseEntity.ok(providerServiceBO.updateProviderServiceIdentifier(id, requestDto));
        } catch (ProviderServiceException e) {
            log.error("ProviderServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (EquipmentException e) {
            log.error("TypeServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderServiceIdentifierException e) {
            log.error("ProviderServiceIdentifierException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(VehicleTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(EquipmentException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderServiceIdentifierException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}