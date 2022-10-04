package br.company.config;


import br.company.models.login.User;
import br.company.repository.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * 
 */
@Configuration
public class UserProvider {

    @Autowired
    private UserRepository usuarioDAO;

    /**
     *
     * https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch04s04.html
     *
     * SCOPE_REQUEST = Per HTTP request
     *
     * SCOPEDPROXYMODE = Create a class-based proxy (uses CGLIB)
     *
     * @return
     */
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS) // or just @RequestScope
    public User customUserDetails() {

        User usuarioLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return usuarioLogado;
    }

}
