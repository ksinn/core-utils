package uz.ksinn.utils.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import uz.ksinn.utils.advice.response.wrapper.FailureResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CoreErrorResponseWriter implements ErrorResponseWriter {

    private final ObjectMapper objectMapper;
    private final String codeFormat;

    public CoreErrorResponseWriter(ObjectMapper objectMapper, String codeFormat) {
        this.objectMapper = objectMapper;
        this.codeFormat = codeFormat;
    }

    @Override
    public void handle(HttpServletResponse response,
                       HttpStatus status,
                       String code,
                       String messege) throws IOException {
        FailureResponse<String> fail = new FailureResponse<>();
        FailureResponse.Data<String> data = new FailureResponse.Data<>();

        data.setCode(codeFormat + code);
        data.setTimestamp(LocalDateTime.now());
        data.setMessage(messege);

        fail.setData(data);

        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        try (Writer writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(fail));
        }
    }
}
