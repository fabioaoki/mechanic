package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.request.DataAuthentication;
import br.com.mechanic.mechanic.request.PasswordRequestDto;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.request.ProviderAccountUpdateRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.service.PasswordServiceBO;
import br.com.mechanic.mechanic.service.ProviderAccountServiceBO;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/provider-accounts")
public class ProviderAccountController {

    @Autowired
    private ProviderAccountServiceBO providerAccountServiceBO;

    @Autowired
    private PasswordServiceBO passwordServiceBO;

    @Autowired
    private AuthenticationManager manager;


    @GetMapping
    public ResponseEntity<Page<ProviderAccountResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(providerAccountServiceBO.findAll(PageRequest.of(page, size)));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid DataAuthentication data) {
        var token = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authentication = manager.authenticate(token);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderAccountById(@PathVariable Long id) {
        try {
            log.info("Fetching provider account with id: " + id);
            return ResponseEntity.ok(providerAccountServiceBO.findById(id));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}/password/{password}")
    public ResponseEntity<Boolean> checkProviderPassword(@PathVariable Long id, @PathVariable String password) {
        try {
            log.info("Checking provider password with id: {}", id);
            boolean exists = passwordServiceBO.matches(id, password);
            return ResponseEntity.ok(exists);
        } catch (PasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProviderAccount(@RequestBody ProviderAccountRequestDto providerAccountRequest) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
        try {
            log.info("Creating a provider account");
            return ResponseEntity.ok(providerAccountServiceBO.save(providerAccountRequest));
        } catch (ProviderAccountTypeException e) {
            log.error("ProviderAccountTypeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            log.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAddressException e) {
            log.error("ProviderAddressException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderPhoneException e) {
            log.error("ProviderPhoneException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (PasswordException e) {
            log.error("PasswordException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProviderAccount(@PathVariable Long id) throws ProviderAccountException {
        try {
            log.info("Deleting provider account with id: {}", id);
            providerAccountServiceBO.changeStatus(id, ProviderAccountStatusEnum.CANCEL);
            return ResponseEntity.ok().build();
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("active/{id}")
    public ResponseEntity<?> activeProviderAccount(@PathVariable Long id) {
        try {
            log.info("Activating provider account with id: {}", id);
            providerAccountServiceBO.changeStatus(id, ProviderAccountStatusEnum.ACTIVE);
            return ResponseEntity.ok().build();
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("password/{id}")
    public ResponseEntity<?> changePasswordProviderAccount(@PathVariable Long id, @RequestBody PasswordRequestDto passwordRequestDto) {
        try {
            log.info("Activating provider account with id: {}", id);
            passwordServiceBO.updateProviderPassword(id, passwordRequestDto);
            return ResponseEntity.ok().build();
        } catch (PasswordException e) {
            log.error("PasswordException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProviderAccount(@PathVariable Long id, @RequestBody ProviderAccountUpdateRequestDto requestDto) {
        try {
            log.info("Updating provider account with id: {}", id);
            return ResponseEntity.ok(providerAccountServiceBO.updateProviderAccount(id, requestDto));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountTypeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(ProviderAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderAccountTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderAddressException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderPhoneException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
    private static ErrorResponse getErrorResponse(PasswordException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
