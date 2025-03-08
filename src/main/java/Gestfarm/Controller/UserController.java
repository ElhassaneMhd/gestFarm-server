package Gestfarm.Controller;

import Gestfarm.Dto.Request.UserRequest;
import Gestfarm.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_USERS')")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/paginate")
    @PreAuthorize("hasAuthority('READ_USERS')")
    public ResponseEntity<Object> paginate(@RequestParam int page ,@RequestParam int limit ) {
        return ResponseEntity.ok(userService.paginate(page,limit));
    }

    @GetMapping("/shippers")
    @PreAuthorize("hasAuthority('READ_USERS')")
    public ResponseEntity<Object> getShippers() {
        return ResponseEntity.ok(userService.findAllShippers());
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_USERS')")
    public ResponseEntity<Object> save(@RequestBody UserRequest userRequest){
        return  userService.save(userRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USERS')")
    public ResponseEntity<Object> delete( @PathVariable Integer id){
        return  userService.delete(id);
    }

    @PreAuthorize("hasAuthority('DELETE_USERS')")
    @PostMapping("/delete/multiple")
    public void multipleDelete(@RequestBody List<Integer> ids){
        userService.multipleDelete(ids);
    }
}
