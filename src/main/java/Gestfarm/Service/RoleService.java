package Gestfarm.Service;

import Gestfarm.Dto.PaginateDTO;
import Gestfarm.Dto.Request.RoleRequest;
import Gestfarm.Dto.RoleDTO;
import Gestfarm.Mapper.RoleMapper;
import Gestfarm.Model.Permission;
import Gestfarm.Model.Role;
import Gestfarm.Repository.PermissionRepository;
import Gestfarm.Repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public PaginateDTO<RoleDTO> paginate(int page , int limit){
        int total = findAll().size();
        Pageable pageable = PageRequest.of(page-1, limit);
        Page<Role> roles= roleRepository.findAll(pageable);
        List<RoleDTO> roleDTOS= roles.stream()
                .map(roleMapper::mapToDto)
                .toList();
        return new PaginateDTO<>(page,limit,total,roleDTOS);
    }

    public Object save(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.name());
        role.setPermissions(roleRequest.permissions());
        return roleRepository.save(role);
    }
    @Transactional
    public Object update(int id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return "Undefined role";
        }
        List<Permission> newPermissions = roleRequest.permissions();
        List<Permission> currentPermissions = role.getPermissions();

        List<Permission> permissionsToRemove = currentPermissions.stream()
                .filter(permission -> !newPermissions.contains(permission))
                .toList();
        permissionsToRemove.forEach(permission ->permission.getRoles().remove(role) );

        role.setPermissions(newPermissions);
        roleRepository.save(role);
        return "Role updated successfully";
    }


}
