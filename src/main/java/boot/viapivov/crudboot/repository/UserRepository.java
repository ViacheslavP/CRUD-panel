package boot.viapivov.crudboot.repository;

import boot.viapivov.crudboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String a);

}
