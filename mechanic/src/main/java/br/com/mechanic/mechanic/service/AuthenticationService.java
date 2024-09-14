package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.repository.provider.ProviderPasswordRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    private final String USER_NOT_FOUND_MSG = "User with email %s not found";

    @Autowired
    private ProviderPasswordRepositoryImpl passwordRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return (UserDetails) passwordRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, login)));
    }
}