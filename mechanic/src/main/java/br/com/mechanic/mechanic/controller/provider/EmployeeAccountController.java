package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.EmployeeAccountException;
import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDtoPage;
import br.com.mechanic.mechanic.service.EmployeeAccountServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/employee-account")
public class EmployeeAccountController {

    @Autowired
    private EmployeeAccountServiceBO employeeAccountServiceBO;

    @GetMapping
    public ResponseEntity<Page<EmployeeAccountResponseDtoPage>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(employeeAccountServiceBO.findAll(PageRequest.of(page, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createEmployeeAccount(
            @RequestBody EmployeeAccountRequest employeeAccountRequest) throws EmployeeAccountException, ProviderAccountException {
        try {
            log.info("Creating a new employee account");
            return ResponseEntity.ok(employeeAccountServiceBO.save(employeeAccountRequest));
        } catch (EmployeeAccountException e) {
            log.error("EmployeeAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            log.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeAccountById(@PathVariable Long id) {
        try {
            log.info("Fetching employee account with id: " + id);
            return ResponseEntity.ok(employeeAccountServiceBO.findById(id));
        } catch (EmployeeAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeeAccount(@PathVariable Long id, @RequestBody EmployeeAccountRequest requestDto) {
        try {
            log.info("Updating employee account with id: " + id);
            return ResponseEntity.ok(employeeAccountServiceBO.updateEmployeeAccount(id, requestDto));
        } catch (EmployeeAccountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(EmployeeAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}
