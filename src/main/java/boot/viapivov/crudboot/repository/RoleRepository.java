package boot.viapivov.crudboot.repository;

import boot.viapivov.crudboot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
