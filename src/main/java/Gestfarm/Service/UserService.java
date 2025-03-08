package Gestfarm.Service;

import Gestfarm.Dto.PaginateDTO;
import Gestfarm.Dto.Request.UserRequest;
import Gestfarm.Dto.Response.RegisterResponse;
import Gestfarm.Dto.UserDTO;
import Gestfarm.Mapper.UserMapper;
import Gestfarm.Model.Role;
import Gestfarm.Model.Shipment;
import Gestfarm.Model.User;
import Gestfarm.Repository.RoleRepository;
import Gestfarm.Repository.UserRepository;
import Gestfarm.Security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    public  UserService (UserMapper userMapper,
                         UserRepository userRepository,
                         JWTService jwtService,
                         AuthenticationManager authManager,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder){
        this.userMapper = userMapper;
        this.authManager= authManager;
        this.passwordEncoder= passwordEncoder;
        this.jwtService= jwtService;
        this.roleRepository= roleRepository;
        this.userRepository=userRepository;
    }

    public List<UserDTO> findAll(){
        Role role = roleRepository.findByName("ROLE_ADMIN");
        List<User> userList= userRepository.findUsersByRoleNot(role);
        return  userList.stream().map(userMapper::mapToDto).toList();
    }
    public PaginateDTO<UserDTO> paginate(int page,int limit) {
        Pageable pageable = PageRequest.of(page-1, limit);
        Page<User> usersList= userRepository.findNonAdminUsers(pageable);
        int total =(int) usersList.getTotalElements();
        List<UserDTO> users = usersList.stream().map(userMapper::mapToDto).toList();
        return new PaginateDTO<>(page,limit,total,users);
    }

    public User findById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public Object findAllShippers() {
        List<User> shippersList = userRepository.findUsersByRole_Name("ROLE_SHIPPER");
        return shippersList.stream().map(userMapper::mapToShipper).toList();
    }

    public ResponseEntity<Object> save(UserRequest request) {
        System.out.println(request);
        if (userRepository.existsByUsername(request.username())) {
            return new ResponseEntity<>("Username is already taken", HttpStatusCode.valueOf(401));
        } else if (userRepository.existsByEmail(request.email())) {
            return new ResponseEntity<>("Email is already taken", HttpStatusCode.valueOf(401));
        } else if (!request.password().equals(request.passwordConfirmation())) {
            return new ResponseEntity<>("Passwords doesn't not match", HttpStatusCode.valueOf(401));
        } else if (userRepository.existsByPhone(request.phone())) {
            return new ResponseEntity<>("Phone number is already in use", HttpStatusCode.valueOf(401));
        }else {
            User user = new User();
            Role role = roleRepository.findByName("ROLE_"+request.role());
            user.setRole(role);
            user.setUsername(request.username());
            user.setEmail(request.email());
            user.setPhone(request.phone());
            user.setPassword(passwordEncoder.encode(request.password()));
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        }
    }


    public ResponseEntity<String> verify(User user) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if (authentication.isAuthenticated()){
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(token);
        }
        return new ResponseEntity<>("Invalid credentials" , HttpStatusCode.valueOf(401));
    }

    @Transactional
    public RegisterResponse  register(UserRequest request)  {
        RegisterResponse rep = new RegisterResponse();
        rep.setStatus(false);
        Role role = roleRepository.findByName("ROLE_USER");
        if (userRepository.existsByUsername(request.username())) {
            rep.setMessage("Username is already taken");
            return rep;
        }

        if (userRepository.existsByEmail(request.email())) {
            rep.setMessage("Email is already taken");
            return rep;
        }

        if (!request.password().equals(request.passwordConfirmation())) {
            rep.setMessage("Passwords doesn't not match");
            return rep;
        }
        if (userRepository.existsByPhone(request.phone())) {
            rep.setMessage("Phone number is already in use");
            return rep;
        }

        if (roleRepository.existsByName(String.valueOf(request.role()))){
            role = roleRepository.findByName("ROLE_"+request.role());
        }
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        User savedUser = userRepository.save(user);


        String token = jwtService.generateToken(savedUser);
        rep.setStatus(true);
        rep.setUser(savedUser);
        rep.setToken(token);
        return rep;
    }

    @Transactional
    public ResponseEntity<Object> delete(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            List<Shipment> shipments= user.get().getShipments();
            shipments.forEach(shipment -> shipment.setShipper(null));
            userRepository.deleteById(id);
            return ResponseEntity.ok("Deleted successfully");
        }
        return new ResponseEntity<>("Cannot delete undefined User" , HttpStatusCode.valueOf(404));
    }

    @Transactional
    public void multipleDelete(List<Integer> ids){
        ids.forEach(this::delete);
    }


}

