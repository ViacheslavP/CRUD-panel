package boot.viapivov.crudboot.controller;

import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        userService.createAdmin();
        return "login";
    }

    @GetMapping(value = "/user")
    public String helloPage(ModelMap modelMap, Authentication authentication) {
        String uname = authentication.getName();
        Optional<UserDto> optionalUser = userService.getByUsername(uname);
        modelMap.addAttribute("newUser", new UserDto());
        modelMap.addAttribute("allRoles", new HashSet<UserDto>());
        optionalUser.ifPresent(x -> modelMap.addAttribute("currentUser", x));

        return "index";
    }

    @GetMapping(value = "/admin")
    public String getMap(ModelMap modelMap, Authentication authentication) {
        String uname = authentication.getName();
        Optional<UserDto> optionalUser = userService.getByUsername(uname);
        optionalUser.ifPresent(x -> modelMap.addAttribute("currentUser", x));
        modelMap.addAttribute("newUser", new UserDto());
        modelMap.addAttribute("users", userService.getAllUsers());
        modelMap.addAttribute("allRoles", userService.getAllRoles());
        return "index";
    }

    @PostMapping(value = "/admin")
    public String postMap(@ModelAttribute UserDto newUser, ModelMap modelMap) {
        userService.add(newUser);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute(name = "newUser") UserDto newUser,
                             ModelMap modelMap) {
        userService.update(newUser);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id,
                             ModelMap modelMap) {
        userService.removeById(id);
        return "redirect:/admin";
    }

}
