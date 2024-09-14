package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.request.DataAuthentication;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @PostMapping()
    public ResponseEntity login(@RequestBody @Valid DataAuthentication data) {
        try {
            var token = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var authentication = manager.authenticate(token);

            return ResponseEntity.ok().build();
        } catch (AuthenticationException ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}
