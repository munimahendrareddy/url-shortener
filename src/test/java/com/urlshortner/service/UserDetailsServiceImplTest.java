package com.urlshortner.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.urlshortner.model.User;
import com.urlshortner.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("admin");
        user.setPassword("password");
    }

    @Test
    void loadUserByUsernameUserFoundTest() {
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository, times(1)).getUserByUsername("admin");
    }

    @Test
    void loadUserByUsernameUserNotFoundTest() {
        when(userRepository.getUserByUsername(anyString())).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });

        String expectedMessage = "Could not find user";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository, times(1)).getUserByUsername("unknown");
    }
}
