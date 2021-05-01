package boot.viapivov.crudboot.service;

import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.model.Role;
import boot.viapivov.crudboot.model.User;
import boot.viapivov.crudboot.repository.RoleRepository;
import boot.viapivov.crudboot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public void createAdmin() {
        boolean exists = true;
        try {
            exists = !userRepository.findAll().isEmpty();
        } catch (Exception e) {
            exists = false;
        }
        if (!exists) {
            Set<Role> adminRole = new HashSet<>() {{
                add(new Role("ROLE_ADMIN"));
                add(new Role("ROLE_USER"));
            }};

            add(UserDto
                    .builder()
                    .username("admin")
                    .password("admin")
                    .roles(adminRole)
                    .build());

        }
    }

    @Transactional
    @Override
    public void add(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user.toUser());
    }

    @Transactional
    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(UserDto user) {
        if (!user.getUsername().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user.toUser());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getByUsername(String uname) {
        UserDto user = UserDto.fromUser(userRepository.findByUsername(uname));
        return Optional.ofNullable(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getById(Long id) {
        UserDto user = UserDto.fromUser(userRepository.getById(id));
        return Optional.ofNullable(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Role> getAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

}