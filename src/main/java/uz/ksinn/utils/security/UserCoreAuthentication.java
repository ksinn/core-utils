package uz.ksinn.utils.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserCoreAuthentication extends AbstractAuthenticationToken {

    private final User user;

    public UserCoreAuthentication(User user) {
        super(user.getAuthorities());
        setAuthenticated(true);
        this.user = user;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
