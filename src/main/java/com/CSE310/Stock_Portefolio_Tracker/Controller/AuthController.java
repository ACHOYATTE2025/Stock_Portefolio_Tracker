package com.CSE310.Stock_Portefolio_Tracker.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.CSE310.Stock_Portefolio_Tracker.Dto.ErroResponseDto;
import com.CSE310.Stock_Portefolio_Tracker.Dto.LoginRequestDto;
import com.CSE310.Stock_Portefolio_Tracker.Dto.ResponseDto;
import com.CSE310.Stock_Portefolio_Tracker.Dto.SignupRequestDto;
import com.CSE310.Stock_Portefolio_Tracker.Entities.Userx;
import com.CSE310.Stock_Portefolio_Tracker.Services.AuthService;
import com.CSE310.Stock_Portefolio_Tracker.Services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
  name = "Stock tracker management",
  description="AUTHENTIFICATION REST Api in Stock tracker management APP to CREATE  details"
)
@RequiredArgsConstructor
@RestController
public class AuthController {
    
private  final AuthService authService;
private final AuthenticationManager authenticationManager;
private final JwtService jwtService;


    //User registration
  
//create
  @Operation(
    summary="REST API to create new User in Stock Tracker mangement",
    description = "REST API to create new Account  inside Stock Tracker mangement "
  )

  @ApiResponse(
    responseCode="201",
    description = "HTTP Status CREATED"
  )
  @PostMapping("/registerUser")
  public ResponseEntity<ResponseDto> registerUser( @RequestBody @Valid SignupRequestDto request)throws Exception {
        return (ResponseEntity<ResponseDto>) this.authService.RegisterUserService(request);
       
  }


  //login

  @Operation(
    summary="REST API to login  user or User into stock portefolio tracker app",
    description = "REST API to user to login stock portefolio tracker app"
  )

  @ApiResponses({
    @ApiResponse(
        responseCode="200",
        description = "HTTP Status DONE",
        content = @Content(
            schema = @Schema(implementation = ResponseDto.class)) ),
    
    @ApiResponse(   

        description = "Login  failed!!!",
        content = @Content(
            schema = @Schema(implementation = ErroResponseDto.class)
        )
    )
    }
  )
@PostMapping("/login")
 public ResponseEntity<String> login( @RequestBody LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken((Userx) authentication);

        return ResponseEntity.ok(token);
    }
}


