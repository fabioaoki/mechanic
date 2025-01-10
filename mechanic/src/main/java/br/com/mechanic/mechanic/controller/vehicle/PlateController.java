package br.com.mechanic.mechanic.controller.vehicle;

import br.com.mechanic.mechanic.exception.PlateException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.service.request.PlateRequest;
import br.com.mechanic.mechanic.service.response.PlateResponseDto;
import br.com.mechanic.mechanic.service.vehicle.PlateServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/plate")
public class PlateController {

    @Autowired
    private PlateServiceBO plateServiceBO;

    @GetMapping
    public ResponseEntity<Page<PlateResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(plateServiceBO.findAll(PageRequest.of(page, size)));
    }

    @PostMapping("/client-account-id/{clientAccountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPlate(
            @RequestBody List<PlateRequest> plateRequests,
            @PathVariable Long clientAccountId) throws PlateException {
        try {
            log.info("Creating a new Plate");
            return ResponseEntity.ok(plateServiceBO.save(plateRequests, clientAccountId));
        } catch (PlateException e) {
            log.error("PlateException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlateById(@PathVariable Long id) {
        try {
            log.info("Fetching plate with id: " + id);
            return ResponseEntity.ok(plateServiceBO.findById(id));
        } catch (PlateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(PlateException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
