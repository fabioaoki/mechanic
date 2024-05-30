package br.com.mechanic.mechanic.controller;

import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.TypeServiceException;
import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.request.TypeServiceRequestDto;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.TypeServiceResponseDto;
import br.com.mechanic.mechanic.response.VehicleTypeResponseDto;
import br.com.mechanic.mechanic.service.TypeServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/type-service")
public class TypeServiceController {

    @Autowired
    private TypeServiceBO typeServiceBO;

    @GetMapping
    public ResponseEntity<Page<TypeServiceResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(typeServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeServiceById(@PathVariable Long id) {
        try {
            log.info("Fetching type service with id: " + id);
            return ResponseEntity.ok(typeServiceBO.findById(id));
        } catch (TypeServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createTypeService(@RequestBody TypeServiceRequestDto typeServiceRequestDto) throws TypeServiceException {
        try {
            log.info("Creating a type service");
            return ResponseEntity.ok(typeServiceBO.save(typeServiceRequestDto));
        } catch (TypeServiceException e) {
            log.error("TypeServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTypeService(@PathVariable Long id, @RequestBody TypeServiceRequestDto requestDto) {
        try {
            log.info("Updating type service with id: " + id);
            return ResponseEntity.ok(typeServiceBO.updateTypeServiceName(id, requestDto));
        } catch (TypeServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(TypeServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}