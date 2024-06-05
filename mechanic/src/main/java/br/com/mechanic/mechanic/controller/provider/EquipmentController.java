package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.request.EquipmentRequestDto;
import br.com.mechanic.mechanic.response.EquipmentResponseDto;
import br.com.mechanic.mechanic.service.EquipmentServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentServiceBO equipmentServiceBO;

    @GetMapping
    public ResponseEntity<Page<EquipmentResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(equipmentServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Long id) {
        try {
            log.info("Fetching equipment with id: " + id);
            return ResponseEntity.ok(equipmentServiceBO.findById(id));
        } catch (EquipmentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createEquipment(@RequestBody EquipmentRequestDto typeServiceRequestDto) throws EquipmentException {
        try {
            log.info("Creating a equipment");
            return ResponseEntity.ok(equipmentServiceBO.save(typeServiceRequestDto));
        } catch (EquipmentException e) {
            log.error("TypeServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTypeService(@PathVariable Long id, @RequestBody EquipmentRequestDto requestDto) {
        try {
            log.info("Updating equipment with id: " + id);
            return ResponseEntity.ok(equipmentServiceBO.updateEquipmentName(id, requestDto));
        } catch (EquipmentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(EquipmentException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}