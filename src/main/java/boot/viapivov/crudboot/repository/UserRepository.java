package boot.viapivov.crudboot.repository;

import boot.viapivov.crudboot.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "user-with-roles")
    User findByUsername(String a);

    @NotNull
    @Query("select user from User user left join fetch user.roles where user.id = :id")
    User getById(@NotNull @Param("id") Long id);

    @NotNull
    @EntityGraph(value = "user-with-roles")
    List<User> findAll();

}
