package boot.viapivov.crudboot.service;

import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.model.Role;
import boot.viapivov.crudboot.repository.RoleRepository;
import boot.viapivov.crudboot.repository.UserRepository;
import boot.viapivov.crudboot.util.Converter;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
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
                    .email("admin@mail.ru")
                    .password("admin")
                    .roles(adminRole)
                    .firstName("admin")
                    .lastName("admin")
                    .build());
        }
    }

    @Transactional
    @Override
    public void add(UserDto user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(Converter.dto2User(user));
    }

    @Transactional
    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(UserDto user) {
        if (!user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(Converter.dto2User(user));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getByUsername(String uname) {

        try {
            UserDto user = Converter.user2Dto(userRepository.findByUsername(uname));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getById(Long id) {
        try {
            UserDto user = Converter.user2Dto(userRepository.getById(id));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(Converter::user2Dto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Role> getAllRoles() {
        return new HashSet<>(roleRepository.findAll());
    }
}
