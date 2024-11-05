package br.com.mechanic.mechanic.config.security;

import br.com.mechanic.mechanic.repository.provider.ProviderPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TokenServiceBo tokenServiceBo;

    @Autowired
    private ProviderPasswordRepository repository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desabilita CSRF para ser stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Sessão stateless
                .addFilterBefore(new AuthenticationToTokenFilter(tokenServiceBo, repository), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(auth -> auth
                                .requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations())).permitAll()  // Exemplo para permitir recursos estáticos
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Permite acesso ao Swagger UI e API Docs
                                .requestMatchers("/auth").permitAll()  // Permite o login sem autenticação

                        // Descomente a linha abaixo para habilitar a autenticação para todos os outros endpoints
                        // .anyRequest().authenticated()  // Todos as outras requisições precisam de autenticação
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
