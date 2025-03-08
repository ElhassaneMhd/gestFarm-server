package Gestfarm.Controller;

import Gestfarm.Dto.Request.RoleRequest;
import Gestfarm.Model.Permission;
import Gestfarm.Model.Role;
import Gestfarm.Repository.PermissionRepository;
import Gestfarm.Repository.RoleRepository;
import Gestfarm.Service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleController(RoleService roleService, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @PreAuthorize("hasAuthority('READ_ROLES')")
    @RequestMapping("/paginate")
    public ResponseEntity<Object> getAll(@RequestParam int page, @RequestParam int limit){
        return ResponseEntity.ok(roleService.paginate(page,limit));
    }

    @PreAuthorize("hasAuthority('READ_ROLES')")
    @RequestMapping("/permissions")
    public ResponseEntity<Object> getAllPermissions(){
        return ResponseEntity.ok(permissionRepository.findAll());
    }
    @PreAuthorize("hasAuthority('WRITE_ROLES')")
    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody RoleRequest roleRequest){
        return ResponseEntity.ok().body( roleService.save(roleRequest));
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable int id, @RequestBody RoleRequest role){
        if (roleRepository.existsById(id)){
            return ResponseEntity.ok(roleService.update(id,role)) ;
        }
        return ResponseEntity.badRequest().body(" undefined Role");
    }
}
