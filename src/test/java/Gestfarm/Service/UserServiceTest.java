package Gestfarm.Service;

import Gestfarm.Dto.Request.UserRequest;
import Gestfarm.Mapper.UserMapper;
import Gestfarm.Model.Role;
import Gestfarm.Model.User;
import Gestfarm.Repository.RoleRepository;
import Gestfarm.Repository.UserRepository;
import Gestfarm.Security.JWTService;
import Gestfarm.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("User Service Tests")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTService jwtService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRequest testUserRequest;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testUser = TestUtils.createTestUser();
        testUserRequest = TestUtils.createTestUserRequest();
        testRole = TestUtils.createTestRole();
    }

    @Test
    @Order(1)
    @DisplayName("Find All Users - Should Return User List")
    void whenFindAll_thenReturnUserList() {
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(new Role());
        when(userRepository.findUsersByRoleNot(any())).thenReturn(Arrays.asList(testUser));
        
        assertNotNull(userService.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("Save User - With Valid Data Should Return User")
    void whenSaveUser_withValidData_thenReturnUser() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhone(any())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(testRole);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(testUser);

        ResponseEntity<?> response = userService.save(testUserRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @Order(3)
    @DisplayName("Delete User - With Valid Id Should Return Success")
    void whenDeleteUser_withValidId_thenReturnSuccess() {
        // Create a user with empty shipments list to avoid NPE
        User user = TestUtils.createTestUser();
        user.setShipments(new java.util.ArrayList<>());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        
        ResponseEntity<?> response = userService.delete(1);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenRegister_withValidData_thenReturnSuccess() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhone(any())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(testRole);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(testUser);
        when(jwtService.generateToken(any())).thenReturn("test-token");

        var response = userService.register(testUserRequest);
        assertTrue(response.getStatus());
    }
}
