package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.config.security.TokenServiceBo;
import br.com.mechanic.mechanic.service.request.DataAuthentication;
import br.com.mechanic.mechanic.service.response.TokenDto;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenServiceBo tokenService;

    @PostMapping()
    public ResponseEntity<TokenDto> login(@RequestBody @Valid DataAuthentication data) {
        try {
            var token = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            Authentication authentication = manager.authenticate(token);
            String tokenjwt = tokenService.generated(authentication);
            System.out.println(tokenjwt);
            return ResponseEntity.ok(new TokenDto(tokenjwt, "Bearer"));
        } catch (AuthenticationException ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
