package boot.viapivov.crudboot.controller;

import boot.viapivov.crudboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        userService.createAdmin();
        return "login.html";
    }

    @GetMapping(value = "/admin")
    public String adminPage() {
        return "index.html";
    }

    @GetMapping(value = "/user")
    public String userPage() {
        return "index.html";
    }

    @GetMapping(value = "/static/js/fillings.js")
    public String javascriptContent() {
        return "../static/js/fillings.js";
    }
}