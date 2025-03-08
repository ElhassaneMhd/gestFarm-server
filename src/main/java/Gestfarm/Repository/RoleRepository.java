package Gestfarm.Repository;

import Gestfarm.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    Boolean existsByName(String name);


}
