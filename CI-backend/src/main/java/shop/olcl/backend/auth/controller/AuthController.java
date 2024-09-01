package shop.olcl.backend.auth.controller;

import com.nimbusds.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.olcl.backend.auth.dto.CognitoUserDto;
import shop.olcl.backend.auth.service.CognitoService;
import shop.olcl.backend.common.annotation.Loggable;
import shop.olcl.backend.user.service.UserService;

@Loggable
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CognitoService cognitoService;
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@AuthenticationPrincipal Jwt jwt,
                                         @RequestBody CognitoUserDto cognitoUserDto) {
        userService.saveOrUpdateUser(cognitoUserDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@AuthenticationPrincipal JWT jwt,
                                        @RequestBody CognitoUserDto cognitoUserDto) {
        userService.saveOrUpdateUser(cognitoUserDto);
        return ResponseEntity.ok("User logged in successfully");
    }
}
