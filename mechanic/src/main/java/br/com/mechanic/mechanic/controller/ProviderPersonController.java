package br.com.mechanic.mechanic.controller;

import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderPersonException;
import br.com.mechanic.mechanic.request.ProviderAccountUpdateRequestDto;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import br.com.mechanic.mechanic.service.ProviderPersonServiceBO;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/provider-person")
public class ProviderPersonController {

    @Autowired
    private ProviderPersonServiceBO personServiceBO;

    @GetMapping
    public ResponseEntity<Page<ProviderPersonResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(personServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderAccountById(@PathVariable Long id) {
        try {
            log.info("Fetching provider person with id: " + id);
            return ResponseEntity.ok(personServiceBO.findById(id));
        } catch (ProviderPersonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProviderAccount(@PathVariable Long id,@RequestBody ProviderPersonUpdateRequest requestDto) {
        try {
            log.info("Updating provider person with id: " + id);
            return ResponseEntity.ok(personServiceBO.updateProviderPerson(id, requestDto));
        } catch (ProviderPersonException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderPersonException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
