package boot.viapivov.crudboot.service;


import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.model.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    void createAdmin();

    void add(UserDto user);

    void removeById(Long id);

    void update(UserDto user);

    Optional<UserDto> getByUsername(String uname);

    Optional<UserDto> getById(Long id);

    List<UserDto> getAllUsers();

    Set<Role> getAllRoles();
}
