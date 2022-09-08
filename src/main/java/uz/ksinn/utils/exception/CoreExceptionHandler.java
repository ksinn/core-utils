package uz.ksinn.utils.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.ksinn.utils.advice.response.wrapper.FailureResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CoreExceptionHandler extends ResponseEntityExceptionHandler {

    private String codeFormat = "%s";

    @Autowired(required = false)
    public void setServiceName(@Value("${ksinn.service.short-name}") String serviceShortName) {
        this.codeFormat = "" + serviceShortName
                .toUpperCase()
                .replace(" ", "_").replace("-", "_")
                + "__%s";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex) {

        if (ex instanceof CoreException) {
            CoreException typedEx = (CoreException) ex;

            return getFail(
                    typedEx.getHttpStatus(),
                    typedEx.getCode(),
                    typedEx.getDescription(),
                    null,
                    null);
        }

        log.error("ERROR {}", ex.getMessage(), ex);

        return getFail(HttpStatus.INTERNAL_SERVER_ERROR,
                "UNKNOWN_ERROR",
                "Unknown error",
                null,
                ex.getMessage());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)  {
        List<String> fieldErrors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> objectErrors = ex.getBindingResult()
                .getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        fieldErrors.addAll(objectErrors);

        return getFail(HttpStatus.BAD_REQUEST,
                null,
                "Validation error",
                fieldErrors,
                null);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        FailureResponse<List<String>> fail = new FailureResponse<>();
        FailureResponse.Data<List<String>> data = new FailureResponse.Data<>();

        data.setCode(String.format(codeFormat, "ERROR"));
        data.setTimestamp(LocalDateTime.now());
        data.setMessage(ex.getMessage());

        fail.setData(data);

        return new ResponseEntity<>(fail, status);
    }

    protected ResponseEntity<Object> getFail(HttpStatus status,
                                             String code,
                                             String message,
                                             List<String> validationCodes,
                                             String ex) {

        FailureResponse<List<String>> fail = new FailureResponse<>();
        FailureResponse.Data<List<String>> data = new FailureResponse.Data<>();

        if (code != null) {
            data.setCode(String.format(codeFormat, code));
        }

        data.setTimestamp(LocalDateTime.now());
        data.setMessage(message);
        data.setValidationCodes(validationCodes);
        data.setException(ex);

        fail.setData(data);

        return new ResponseEntity<>(fail, status);
    }
}
