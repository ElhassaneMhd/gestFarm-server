package Gestfarm.Dto;

import Gestfarm.Model.Permission;
import Gestfarm.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private int id;
    private String name;
    private List<Permission> permissions;
    private List<User> users;
}
