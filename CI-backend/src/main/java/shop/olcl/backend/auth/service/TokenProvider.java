package shop.olcl.backend.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private static String uri = "https://cognito-idp.ap-northeast-2.amazonaws.com/ap-northeast-2_dcwrwaxzr/.well-known/jwks.json";

    public JwtDecoder accessTokenDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(uri).build();
    }
}
