package Gestfarm.Mapper;

import Gestfarm.Dto.RoleDTO;
import Gestfarm.Model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleDTO mapToDto(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setPermissions(role.getPermissions());
        roleDTO.setUsers(role.getUser());
        return roleDTO;
    }
}
