package Gestfarm.Security;

import Gestfarm.Model.Permission;
import Gestfarm.Model.Role;
import Gestfarm.Model.User;
import Gestfarm.Repository.PermissionRepository;
import Gestfarm.Repository.RoleRepository;
import Gestfarm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {

        if (alreadySetup) return;
        List<String> entities = List.of("SHEEP", "USERS", "SHIPMENTS", "SALES", "CATEGORIES", "PERMISSIONS", "ROLES");
        List<String> actions = List.of("READ", "WRITE", "UPDATE", "DELETE");

        Map<String, List<Permission>> entityPermissions = new HashMap<>();

        for (String entity : entities) {
            List<Permission> permissions = actions.stream()
                    .map(action -> createPermissionIfNotFound(action + "_" + entity))
                    .toList();
            entityPermissions.put(entity.toLowerCase() + "Permissions", permissions);
        }
        List<Permission> farmerPermissions = Stream.of(
                entityPermissions.get("sheepPermissions"),
                entityPermissions.get("salesPermissions"),
                entityPermissions.get("categoriesPermissions"),
                entityPermissions.get("salesPermissions")
        ).flatMap(List::stream).toList();

        List<Permission> shipperPermissions = entityPermissions.get("shipmentsPermissions");
        List<Permission> userPermissions = List.of();
        List<Permission> adminPermissions = entityPermissions.values().stream().flatMap(List::stream).toList();

        createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
        createRoleIfNotFound("ROLE_USER", userPermissions);
        createRoleIfNotFound("ROLE_FARMER" ,farmerPermissions);
        createRoleIfNotFound("ROLE_SHIPPER" ,shipperPermissions);

        List<Role> roles = roleRepository.findAll(); // Fetch all roles

        roles.forEach(role -> {
            String username = role.getName().replace("ROLE_", "").toLowerCase();

            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("password123"));
            user.setEmail(username + "@gmail.com");
            user.setPhone("06665555"+role.getId());
            user.setRole(role);

            if (!userRepository.existsByUsernameOrEmailOrPhone(user.getUsername(),user.getEmail(),user.getPhone())) {
                userRepository.save(user);
            }
        });
        alreadySetup = true;
    }

    @Transactional
    Permission createPermissionIfNotFound(String name) {

        Permission privilege = permissionRepository.findByName(name);
        if (privilege == null) {
            privilege = new Permission(name);
            permissionRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    void createRoleIfNotFound(
            String name, List<Permission> permissions) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPermissions(permissions);
            roleRepository.save(role);
        }
    }
}