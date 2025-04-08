package Gestfarm.Security;

import Gestfarm.Model.User;
import Gestfarm.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @Mock
    private ApplicationContext context;

    @Mock
    private MyUserDetailsService userDetailsService;

    @InjectMocks
    private JWTService jwtService;

    private User testUser;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        testUser = TestUtils.createTestUser();
        userDetails = new UserDetail(testUser);
        
        // Set the secret key for JWT service
        ReflectionTestUtils.setField(jwtService, "jwtSecretKey", 
            "GounL3uJzHdDDLq32j3rs3IyPZyBWpi6jG6LObSDjv0R7bH0t8XIkPkCgUHDrQbI");
        
        when(context.getBean(MyUserDetailsService.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
    }

    @Test
    void whenGenerateToken_thenReturnValidToken() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void whenValidateToken_withValidToken_thenReturnTrue() {
        String token = jwtService.generateToken(testUser);
        boolean isValid = jwtService.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void whenExtractUsername_thenReturnCorrectUsername() {
        String token = jwtService.generateToken(testUser);
        String username = jwtService.extractUserName(token);
        assertEquals(testUser.getUsername(), username);
    }
}
