package Gestfarm.Repository;

import Gestfarm.Model.Role;
import Gestfarm.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    boolean existsByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findUsersByRoleNot(Role role);
    List<User> findUsersByRole_Name(String role_name);

    boolean existsByUsernameOrEmailOrPhone(String username,String email,String phone);

    @Query("SELECT u FROM User u WHERE u.role.name <> 'ROLE_ADMIN'")
    Page<User> findNonAdminUsers(Pageable pageable);
}

