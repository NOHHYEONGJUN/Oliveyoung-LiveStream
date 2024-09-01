package shop.olcl.backend.auth.service;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.olcl.backend.auth.dto.CognitoUserDto;
import shop.olcl.backend.common.annotation.Loggable;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;

@Loggable
@RequiredArgsConstructor
@Service
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;

    public GetUserResponse authenticateToken(String accessToken) {
        // 'Bearer '로 시작하는 경우 이를 제거
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        try {
            GetUserRequest getUserRequest = GetUserRequest.builder()
                    .accessToken(accessToken)
                    .build();

            return cognitoClient.getUser(getUserRequest);

        } catch (CognitoIdentityProviderException e) {
            throw new RuntimeException("Failed to authenticate token with Cognito", e);
        }
    }

    public CognitoUserDto getUserInfo(GetUserResponse getUserResponse) {
        Map<String, String> userAttributes = extractUserAttributes(getUserResponse);

        return CognitoUserDto.builder()
                .cognitoUserId(getUserResponse.username())
                .email(userAttributes.get("email"))
                .username(userAttributes.get("name"))
                .gender(userAttributes.get("gender"))
                .age(Integer.parseInt(userAttributes.get("age")))
                .build();
    }

    private Map<String, String> extractUserAttributes(GetUserResponse getUserResponse) {
        return getUserResponse.userAttributes().stream()
                .collect(Collectors.toMap(AttributeType::name, AttributeType::value));
    }
}
