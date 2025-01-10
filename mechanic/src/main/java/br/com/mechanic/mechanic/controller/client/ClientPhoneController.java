package br.com.mechanic.mechanic.controller.client;

import br.com.mechanic.mechanic.exception.ClientPhoneException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.service.request.ClientPhoneRequest;
import br.com.mechanic.mechanic.service.response.ClientPhoneResponseDto;
import br.com.mechanic.mechanic.service.client.ClientPhoneServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/client-phone")
public class ClientPhoneController {
    @Autowired
    private ClientPhoneServiceBO phoneServiceBO;

    @GetMapping
    public ResponseEntity<Page<ClientPhoneResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(phoneServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/client-phone-id/{clientAccountId}")
    public ResponseEntity<?> getClientPhoneByClientAccountId(@PathVariable Long clientAccountId) {
        try {
            log.info("Fetching client phone with clientAccountId: " + clientAccountId);
            return ResponseEntity.ok(phoneServiceBO.findByClientAccountId(clientAccountId));
        } catch (ClientPhoneException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientPhoneById(@PathVariable Long id) {
        try {
            log.info("Fetching client phone with id: " + id);
            return ResponseEntity.ok(phoneServiceBO.findById(id));
        } catch (ClientPhoneException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClientPhone(@PathVariable Long id, @RequestBody ClientPhoneRequest requestDto) {
        try {
            log.info("Updating client phone with id: " + id);
            return ResponseEntity.ok(phoneServiceBO.updateClientPhone(id, requestDto));
        } catch (ClientPhoneException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ClientPhoneException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}