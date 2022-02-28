package uz.ksinn.utils.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import uz.ksinn.utils.security.SecurityConstants;
import uz.ksinn.utils.security.User;
import uz.ksinn.utils.security.UserCoreAuthentication;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final String oauth2SymmetricSecret;

    public JwtAuthenticationProvider(String oauth2SymmetricSecret) {
        this.oauth2SymmetricSecret = oauth2SymmetricSecret;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtCoreAuthenticationToken token = (JwtCoreAuthenticationToken) authentication;

        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(SecurityConstants.JWT_ISSUER)
                .build();

        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token.getValue());
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("Token verification error", e);
        }

        User user = new User(
                decodedJWT.getClaim("username").asString(),
                decodedJWT.getClaim("user_id").asLong(),
                decodedJWT.getClaim("services").asList(String.class)
        );

        return new UserCoreAuthentication(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtCoreAuthenticationToken.class.equals(authentication);
    }

    private Algorithm getAlgorithm() {
        Algorithm algorithm = Algorithm.HMAC256(oauth2SymmetricSecret.getBytes());
        return algorithm;
    }
}
