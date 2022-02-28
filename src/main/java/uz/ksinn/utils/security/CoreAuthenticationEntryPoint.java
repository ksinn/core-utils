package uz.ksinn.utils.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CoreAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ErrorResponseWriter responseWriter;

    public CoreAuthenticationEntryPoint(ErrorResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        responseWriter.handle(response, HttpStatus.UNAUTHORIZED, "AUTH_ERROR", authException.getMessage());
    }
}
