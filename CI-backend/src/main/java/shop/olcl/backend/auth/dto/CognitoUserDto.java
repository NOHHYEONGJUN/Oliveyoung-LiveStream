package shop.olcl.backend.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CognitoUserDto {
    private String cognitoUserId;
    private String email;
    private String username;
    private String gender;
    private int age;
}
