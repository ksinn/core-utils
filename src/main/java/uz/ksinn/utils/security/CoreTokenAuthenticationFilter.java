package uz.ksinn.utils.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.ksinn.utils.security.jwt.JwtCoreAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CoreTokenAuthenticationFilter extends OncePerRequestFilter {

    private ErrorResponseWriter responseWriter;
    private final AuthenticationManager authenticationManager;

    public CoreTokenAuthenticationFilter(ErrorResponseWriter responseWriter, AuthenticationManager authenticationManager) {
        this.responseWriter = responseWriter;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            JwtCoreAuthenticationToken authRequest = convert(request);
            if (authRequest == null) {
                chain.doFilter(request, response);
                return;
            }

            Authentication authResult = this.authenticationManager.authenticate(authRequest);


            SecurityContextHolder.getContext().setAuthentication(authResult);

            onSuccessfulAuthentication(request, response, chain, authResult);

        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            onUnsuccessfulAuthentication(request, response, failed);

            chain.doFilter(request, response);

            return;
        }

        chain.doFilter(request, response);
    }

    private JwtCoreAuthenticationToken convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return null;
        }

        String token = header.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");

        return new JwtCoreAuthenticationToken(token);
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        responseWriter.handle(response,
                HttpStatus.UNAUTHORIZED,
                "AUTH_ERROR",
                failed.getMessage());
    }

}