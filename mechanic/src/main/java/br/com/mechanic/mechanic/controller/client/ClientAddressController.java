package br.com.mechanic.mechanic.controller.client;

import br.com.mechanic.mechanic.exception.ClientAddressException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.request.ClientAddressRequest;
import br.com.mechanic.mechanic.response.ClientAddressResponseByControllerDto;
import br.com.mechanic.mechanic.service.client.ClientAddressServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/client-address")
public class ClientAddressController {
    @Autowired
    private ClientAddressServiceBO clientAddressServiceBO;

    @GetMapping
    public ResponseEntity<Page<ClientAddressResponseByControllerDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(clientAddressServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/client-account-id/{clientAccountId}")
    public ResponseEntity<?> getClientAddressByClientAccountId(@PathVariable Long clientAccountId) {
        try {
            log.info("Fetching client address with clientAccountId: " + clientAccountId);
            return ResponseEntity.ok(clientAddressServiceBO.findByClientAccountId(clientAccountId));
        } catch (ClientAddressException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientAddressById(@PathVariable Long id) {
        try {
            log.info("Fetching client address with id: " + id);
            return ResponseEntity.ok(clientAddressServiceBO.findById(id));
        } catch (ClientAddressException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClientAddress(@PathVariable Long id, @RequestBody ClientAddressRequest requestDto) {
        try {
            log.info("Updating client address with id: " + id);
            return ResponseEntity.ok(clientAddressServiceBO.updateClientAddress(id, requestDto));
        } catch (ClientAddressException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ClientAddressException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}