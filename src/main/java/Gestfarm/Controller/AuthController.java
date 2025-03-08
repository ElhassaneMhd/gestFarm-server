package Gestfarm.Controller;

import Gestfarm.Dto.Request.UserRequest;
import Gestfarm.Dto.Response.RegisterResponse;
import Gestfarm.Dto.UserDTO;
import Gestfarm.Mapper.UserMapper;
import Gestfarm.Model.User;
import Gestfarm.Repository.UserRepository;
import Gestfarm.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/user")
    public UserDTO user( Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        return userMapper.mapToDto(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return userService.verify(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody UserRequest userRequest) {
        RegisterResponse res = userService.register(userRequest);
        if (res.getStatus()){
            return  ResponseEntity.ok(res.getToken());
        }
        return new ResponseEntity<>(res.getMessage(), HttpStatusCode.valueOf(404));
    }
}
