package uz.ksinn.utils.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class User implements UserDetails {

    private final String username;
    private final Long userId;
    private final List<String> authorities;

    public User(String username, Long userId, List<String> authorities) {
        this.username = username;
        this.userId = userId;
        this.authorities = Optional.ofNullable(authorities).orElseGet(List::of);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
                .stream()
                .map(m -> (GrantedAuthority) () -> String.format("ROLE_%s", m.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
