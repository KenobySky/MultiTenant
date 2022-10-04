package br.company.services;


import br.company.models.login.User;
import br.company.repository.login.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * Utilizado na classe SecurityConfiguration.java para indicar como o usuario é carregado.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userDAO;

    @Override
    public User loadUserByUsername(String username) {
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        
        return user.get();
    }
}
