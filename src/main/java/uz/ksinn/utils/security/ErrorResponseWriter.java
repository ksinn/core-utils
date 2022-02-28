package uz.ksinn.utils.security;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ErrorResponseWriter {

    void handle(HttpServletResponse response,
                HttpStatus status,
                String code,
                String messege) throws IOException;
}
