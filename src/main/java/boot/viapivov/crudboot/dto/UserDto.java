package boot.viapivov.crudboot.dto;

import boot.viapivov.crudboot.model.Role;
import boot.viapivov.crudboot.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Integer age;
    private Set<Role> roles;

    public User toUser() {
        return new User(
                id,
                email,
                password,
                firstName,
                lastName,
                age,
                roles);
    }

    public static UserDto fromUser(User user) {
        if (user == null) {
            return null;
        } else {
            return builder()
                    .id(user.getId())
                    .email(user.getUsername())
                    .password(user.getPassword())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .age(user.getAge())
                    .roles(user.getRoles())
                    .build();
        }
    }
}
