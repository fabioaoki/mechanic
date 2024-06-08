package br.com.mechanic.mechanic.controller.vehicle;

import br.com.mechanic.mechanic.exception.ColorException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.request.ColorRequest;
import br.com.mechanic.mechanic.response.ColorResponseDto;
import br.com.mechanic.mechanic.service.ColorServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/color")
public class ColorController {

    @Autowired
    private ColorServiceBO colorServiceBO;

    @GetMapping
    public ResponseEntity<Page<ColorResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(colorServiceBO.findAll(PageRequest.of(page, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createColor(
            @RequestBody ColorRequest colorRequest) throws ColorException {
        try {
            log.info("Creating a new color");
            return ResponseEntity.ok(colorServiceBO.save(colorRequest));
        } catch (ColorException e) {
            log.error("ColorException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorById(@PathVariable Long id) {
        try {
            log.info("Fetching color with id: " + id);
            return ResponseEntity.ok(colorServiceBO.findById(id));
        } catch (ColorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ColorException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
