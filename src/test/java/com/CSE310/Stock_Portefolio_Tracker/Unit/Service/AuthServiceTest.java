package com.CSE310.Stock_Portefolio_Tracker.Unit.Service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.CSE310.Stock_Portefolio_Tracker.Dto.ResponseDto;
import com.CSE310.Stock_Portefolio_Tracker.Dto.SignupRequestDto;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Role;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.Enum.TypeRole;
import com.CSE310.Stock_Portefolio_Tracker.Repository.RoleRepository;
import com.CSE310.Stock_Portefolio_Tracker.Repository.UserxRepository;
import com.CSE310.Stock_Portefolio_Tracker.Services.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

   @Mock
    private UserxRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService; // le mock est injecté ici

    @Test
    void RegisterUserServiceTest() {

        SignupRequestDto user = new SignupRequestDto("acho","acho@gmail.com","acho");

        // mock du repository
        Mockito.when(userRepository.existsByEmail("acho@gmail.com")).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(Userx.class)))
               .thenAnswer(i -> i.getArgument(0));

        // mock du rôle
        Role role = new Role();
        role.setLibele(TypeRole.USER);
        Mockito.when(roleRepository.findByLibele(TypeRole.USER))
               .thenReturn(Optional.of(role));

        // mock du password encoder
        Mockito.when(passwordEncoder.encode("acho")).thenReturn("encodedPassword");

        // when
        ResponseEntity<ResponseDto> saved = authService.RegisterUserService(user);

        // then
        assertNotNull(saved);
        assert(saved.getBody().getStatusMsg().contains("USER CREATED SUCCESSFULLY"));
    
}
}
