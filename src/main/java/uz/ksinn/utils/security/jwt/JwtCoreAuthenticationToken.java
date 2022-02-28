package uz.ksinn.utils.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class JwtCoreAuthenticationToken extends AbstractAuthenticationToken {

    private final String value;

    public JwtCoreAuthenticationToken(String token) {
        super(List.of());
        this.value = token;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object getCredentials() {
        return value;
    }

    @Override
    public Object getPrincipal() {
        return value;
    }
}
