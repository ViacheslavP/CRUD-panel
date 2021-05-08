package boot.viapivov.crudboot.controller;

import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public ResponseEntity<?> loginPage() {
        userService.createAdmin();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<UserDto> helloPage(Authentication authentication) {
        String uname = authentication.getName();
        Optional<UserDto> optionalUser = userService.getByUsername(uname);

        return optionalUser
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/admin")
    public ResponseEntity<List<UserDto>> adminPage() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto newUser) {
        if (userService.getByUsername(newUser.getEmail()).isPresent()) {
            return new ResponseEntity<>(newUser, HttpStatus.CONFLICT);
        } else {
            userService.add(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
    }

    @PostMapping("/admin/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                             @RequestBody UserDto existingUser) {
        userService.update(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    @PostMapping("/admin/delete/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long id) {
        ResponseEntity<UserDto> responseEntity = userService.getById(id)
                .map(userDto -> new ResponseEntity<>(userDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        userService.removeById(id);
        return responseEntity;
    }

}
