package br.company.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * 
 */
@Controller
public class Login {

    public Login() {
    }

    @RequestMapping(value = {"/login", "/login.html"}, method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        
        if (error != null) {
            model.addAttribute("error", "E-mail ou senha incorreta!");
        }

        if (logout != null) {
            model.addAttribute("logout", "Você saiu do sistema (log-out)!");
        }
        
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        return "redirect:/login?logout=true"; //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

}
