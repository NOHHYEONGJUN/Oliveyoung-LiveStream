package shop.olcl.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.olcl.backend.auth.dto.CognitoUserDto;
import shop.olcl.backend.common.entity.BaseTimeEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    private UUID id;

    @Column(name = "cognito_user_id", nullable = false, unique = true)
    private String cognitoUserId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    public User(CognitoUserDto cognitoUserDto) {
        this.id = UUID.fromString(cognitoUserDto.getCognitoUserId());
        this.cognitoUserId = cognitoUserDto.getCognitoUserId();
        this.email = cognitoUserDto.getEmail();
        this.username = cognitoUserDto.getUsername();
        this.gender = cognitoUserDto.getGender();
        this.age = cognitoUserDto.getAge();
    }

    public void update(CognitoUserDto cognitoUserDto) {
        this.cognitoUserId = cognitoUserDto.getCognitoUserId();
        this.email = cognitoUserDto.getEmail();
        this.username = cognitoUserDto.getUsername();
        this.gender = cognitoUserDto.getGender();
        this.age = cognitoUserDto.getAge();
    }
}
