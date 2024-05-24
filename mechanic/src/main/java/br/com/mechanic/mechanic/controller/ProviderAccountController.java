package br.com.mechanic.mechanic.controller;

import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.request.ProviderAccountUpdateRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.service.ProviderAccountServiceBO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider-accounts")
public class ProviderAccountController {

    private static final Logger logger = LogManager.getLogger(ProviderAccountController.class);

    @Autowired
    private ProviderAccountServiceBO providerAccountServiceBO;


    @GetMapping
    public ResponseEntity<Page<ProviderAccountResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(providerAccountServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderAccountById(@PathVariable Long id) {
        try {
            logger.info("Fetching provider account with id: " + id);
            return ResponseEntity.ok(providerAccountServiceBO.findById(id));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProviderAccount(@RequestBody ProviderAccountRequestDto providerAccountRequest) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
        try {
            logger.info("Creating a provider account");
            return ResponseEntity.ok(providerAccountServiceBO.save(providerAccountRequest));
        } catch (ProviderAccountTypeException e) {
            logger.error("ProviderAccountTypeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            logger.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAddressException e) {
            logger.error("ProviderAddressException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderPhoneException e) {
            logger.error("ProviderPhoneException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProviderAccount(@PathVariable Long id) throws ProviderAccountException {
        try {
            logger.info("Deleting provider account with id: " + id);
            providerAccountServiceBO.changeStatus(id, ProviderAccountStatusEnum.CANCEL);
            return ResponseEntity.ok().build();
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("active/{id}")
    public ResponseEntity<?> activeProviderAccount(@PathVariable Long id) {
        try {
            logger.info("Activating provider account with id: " + id);
            providerAccountServiceBO.changeStatus(id, ProviderAccountStatusEnum.ACTIVE);
            return ResponseEntity.ok().build();
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProviderAccount(@PathVariable Long id, @RequestBody ProviderAccountUpdateRequestDto requestDto) {
        try {
            logger.info("Updating provider account with id: " + id);
            return ResponseEntity.ok(providerAccountServiceBO.updateProviderAccount(id, requestDto));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
    private static ErrorResponse getErrorResponse(ProviderAccountTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderAddressException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderPhoneException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
