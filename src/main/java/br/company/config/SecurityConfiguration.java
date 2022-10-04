package br.company.config;

import br.company.services.MyUserDetailsService;
import br.company.tools.MyPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public SecurityConfiguration() {
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceBean());

        return provider;
    }

    @Override
    @Bean
    public MyUserDetailsService userDetailsServiceBean() throws Exception {
        return new MyUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .httpBasic();

        http.logout().permitAll();

        //
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler(new SecurityContextLogoutHandler())
                .permitAll()
                .clearAuthentication(true)
        );

    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        super.configure(web);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        super.configure(auth);
    }

}
