package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.EmployeeAccountException;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.request.EquipmentInUpdateRequest;
import br.com.mechanic.mechanic.request.EquipmentOutRequest;
import br.com.mechanic.mechanic.request.EquipmentOutUpdateRequest;
import br.com.mechanic.mechanic.service.EquipmentOutServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/equipment-out")
public class EquipmentOutController {

    @Autowired
    private EquipmentOutServiceBO equipmentOutServiceBO;

    @GetMapping
    public ResponseEntity<Page<?>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(equipmentOutServiceBO.findAll(PageRequest.of(page, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createEquipmentOut(
            @RequestBody EquipmentOutRequest equipmentOutRequest) throws EquipmentException, ProviderAccountException {
        try {
            log.info("Creating a equipment out");
            return ResponseEntity.ok(equipmentOutServiceBO.save(equipmentOutRequest));
        } catch (EquipmentException e) {
            log.error("EquipmentException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            log.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (EmployeeAccountException e) {
            log.error("EmployeeAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentInById(@PathVariable Long id) {
        try {
            log.info("Fetching equipmentIn with id: " + id);
            return ResponseEntity.ok(equipmentOutServiceBO.findById(id));
        } catch (EquipmentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/provider-account/{provider-account-id}")
    public ResponseEntity<?> findAllByProviderAccountId(
            @PathVariable("provider-account-id") Long providerAccountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            return ResponseEntity.ok(equipmentOutServiceBO.findAllByProviderAccountId(providerAccountId, PageRequest.of(page, size)));
        } catch (EquipmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipmentOut(@PathVariable Long id, @RequestBody EquipmentOutUpdateRequest requestDto) {
        try {
            log.info("Updating equipmentOut with id: " + id);
            return ResponseEntity.ok(equipmentOutServiceBO.updateEquipmentOut(id, requestDto));
        } catch (EquipmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            log.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(EquipmentException e) {
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

    private static ErrorResponse getErrorResponse(EmployeeAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
