package Gestfarm.Dto.Request;

import Gestfarm.Model.Permission;

import java.util.List;

public record RoleRequest(String name ,int id, List<Permission> permissions ) {
}
