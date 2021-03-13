package web.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import web.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ViewController {
    private UserService service;
    public ViewController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return "redirect:/login";
        } else if (auth.getAuthorities().contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String user(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long id = service.getUserByEmail(auth.getName()).getId();
        response.addCookie(new Cookie("UserId", "" + id));
        Boolean isAdmin = service.isAdmin(auth.getName());
        response.addCookie(new Cookie("isAdmin", "" + isAdmin));
        return "index";
    }

    @GetMapping("/admin")
    public String admin(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long id = service.getUserByEmail(auth.getName()).getId();
        response.addCookie(new Cookie("UserId", "" + id));
        Boolean isAdmin = service.isAdmin(auth.getName());
        response.addCookie(new Cookie("isAdmin", "" + isAdmin));
        return "index";
    }
}