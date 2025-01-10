package br.com.mechanic.mechanic.controller.vehicle;

import br.com.mechanic.mechanic.exception.ModelException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.service.response.ModelResponseDto;
import br.com.mechanic.mechanic.service.vehicle.ModelServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelServiceBO modelServiceBO;

    @GetMapping
    public ResponseEntity<Page<ModelResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(modelServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModelById(@PathVariable Long id) {
        try {
            log.info("Fetching model with id: " + id);
            return ResponseEntity.ok(modelServiceBO.findById(id));
        } catch (ModelException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/model/{model}/year/{year}")
    public ResponseEntity<?> getModelById(@PathVariable String model, @PathVariable String year,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        try {
            log.info("Fetching model with model: {} and year: {}", model, year);
            return ResponseEntity.ok(modelServiceBO.findByModelAndYear(model, year, PageRequest.of(page, size)));
        } catch (ModelException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{marc}/year/{year}")
    public ResponseEntity<?> getMarcAndYearById(@PathVariable String marc, @PathVariable String year,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        try {
            log.info("Fetching model with marc: {} and year: {}", marc, year);
            return ResponseEntity.ok(modelServiceBO.findByMarcAndYear(marc, year, PageRequest.of(page, size)));
        } catch (ModelException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ModelException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
