package boot.viapivov.crudboot.controller;

import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
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
        optionalUser.ifPresent(x -> modelMap.addAttribute("user", x));

        return "hello";
    }

    @GetMapping(value = "/admin")
    public String getMap(ModelMap modelMap) {

        modelMap.addAttribute("user", new UserDto());
        modelMap.addAttribute("users", userService.getAllUsers());
        modelMap.addAttribute("all_roles", userService.getAllRoles());

        return "admin";
    }

    @PostMapping(value = "/admin")
    public String postMap(@ModelAttribute UserDto user, ModelMap modelMap) {
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/update/{id}")
    public String updateUserView(@PathVariable("id") long id,
               ModelMap modelMap) {
        Optional<UserDto> user = userService.getById(id);

        modelMap.addAttribute("all_roles", userService.getAllRoles());
        user.ifPresent(x -> modelMap.addAttribute("user1", x));
        return user.isEmpty() ? "redirect:/admin" : "update";
    }


    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute UserDto user,
                             ModelMap modelMap) {
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id,
                             ModelMap modelMap) {
        userService.removeById(id);
        return "redirect:/admin";
    }



}
