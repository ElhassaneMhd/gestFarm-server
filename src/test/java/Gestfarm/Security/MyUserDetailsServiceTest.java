package Gestfarm.Security;

import Gestfarm.Model.User;
import Gestfarm.Repository.UserRepository;
import Gestfarm.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = TestUtils.createTestUser();
    }

    @Test
    void whenLoadByUsername_withValidUsername_thenReturnUserDetails() {
        when(userRepository.findByUsername(anyString())).thenReturn(testUser);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
        assertNotNull(userDetails);
        assertEquals(testUser.getUsername(), userDetails.getUsername());
    }

    @Test
    void whenLoadByUsername_withValidEmail_thenReturnUserDetails() {
        when(userRepository.findByEmail(anyString())).thenReturn(testUser);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        assertNotNull(userDetails);
        assertEquals(testUser.getUsername(), userDetails.getUsername());
    }

    @Test
    void whenLoadByUsername_withInvalidCredentials_thenThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        
        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("invalid"));
    }
}
