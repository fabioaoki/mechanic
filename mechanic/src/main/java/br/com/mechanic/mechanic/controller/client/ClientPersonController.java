package br.com.mechanic.mechanic.controller.client;

import br.com.mechanic.mechanic.exception.ClientPersonException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.service.request.ClientPersonRequest;
import br.com.mechanic.mechanic.service.response.ClientPersonResponseByControllerDto;
import br.com.mechanic.mechanic.service.client.ClientPersonServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/client-person")
public class ClientPersonController {
    @Autowired
    private ClientPersonServiceBO clientPersonServiceBO;

    @GetMapping
    public ResponseEntity<Page<ClientPersonResponseByControllerDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(clientPersonServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/client-person-id/{clientAccountId}")
    public ResponseEntity<?> getClientPersonByClientAccountId(@PathVariable Long clientAccountId) {
        try {
            log.info("Fetching client person with clientAccountId: " + clientAccountId);
            return ResponseEntity.ok(clientPersonServiceBO.findByClientAccountId(clientAccountId));
        } catch (ClientPersonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientPersonById(@PathVariable Long id) {
        try {
            log.info("Fetching client person with id: " + id);
            return ResponseEntity.ok(clientPersonServiceBO.findById(id));
        } catch (ClientPersonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClientPerson(@PathVariable Long id, @RequestBody ClientPersonRequest requestDto) {
        try {
            log.info("Updating client person with id: " + id);
            return ResponseEntity.ok(clientPersonServiceBO.updateClientPerson(id, requestDto));
        } catch (ClientPersonException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ClientPersonException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}