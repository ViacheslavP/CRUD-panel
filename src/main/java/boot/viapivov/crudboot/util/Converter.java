package boot.viapivov.crudboot.util;

import boot.viapivov.crudboot.dto.UserDto;
import boot.viapivov.crudboot.model.User;

public class Converter {

    public static User dto2User(UserDto userDto) {
        return userDto == null ? null : new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getAge(),
                userDto.getRoles());
    }

    public static UserDto user2Dto(User user) {
        return user == null ? null : UserDto
                .builder()
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
