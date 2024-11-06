package br.com.mechanic.mechanic.config.security;

import br.com.mechanic.mechanic.entity.provider.ProviderPassword;
import br.com.mechanic.mechanic.repository.provider.ProviderPasswordRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthenticationToTokenFilter extends OncePerRequestFilter {

    private TokenServiceBo tokenServiceBo;
    private ProviderPasswordRepository repository;

    public AuthenticationToTokenFilter(TokenServiceBo tokenServiceBo, ProviderPasswordRepository repository){
        this.tokenServiceBo = tokenServiceBo;
        this.repository = repository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);
        boolean valid = tokenServiceBo.isValidToken(token);
        if(valid){
            authenticateClient(token);
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateClient(String token) {
        Long providerId = tokenServiceBo.getIdProvider(token);
        ProviderPassword providerPassword = repository.findById(providerId).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(providerPassword, null, providerPassword.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recoverToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
