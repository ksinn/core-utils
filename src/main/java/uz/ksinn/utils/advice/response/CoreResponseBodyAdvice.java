package uz.ksinn.utils.advice.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import uz.ksinn.utils.advice.response.wrapper.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CoreResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> httpMessageConverter,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body == null ||
                body instanceof String ||
                body instanceof FailureResponse ||
                body instanceof SuccessResponse ||
                body instanceof byte[] ||
                Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseBinding.class))
            return body;

        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        HttpStatus status = HttpStatus.valueOf(httpResponse.getStatus());
        boolean isSuccess = status.is2xxSuccessful();

        ApiResponse apiResponse;
        if (isSuccess)
            apiResponse = getSuccessResponse(body);
        else
            return body;
//            apiResponse = getFailureResponse(body);

        return getOrCreateContainer(apiResponse);
    }

    private MappingJacksonValue getOrCreateContainer(Object body) {
        return body instanceof MappingJacksonValue ?
                (MappingJacksonValue) body :
                new MappingJacksonValue(body);
    }

    private ApiResponse getFailureResponse(Object body) {
        FailureResponse<Object> failureResponse = new FailureResponse<>();
        FailureResponse.Data<Object> data = new FailureResponse.Data<>();
        data.setCode("");
        data.setTimestamp(LocalDateTime.now());

        return failureResponse;
    }

    private ApiResponse getSuccessResponse(Object body) {
        ApiResponse apiResponse;

        if (body instanceof Iterable) {
            SuccessResponse<SuccessDataIterable<?>> successResponse = new SuccessResponse<>();
            SuccessDataIterable<?> successData = new SuccessDataIterable<>((Iterable<?>) body);
            successResponse.setData(successData);

            apiResponse = successResponse;
        } else if (body instanceof Optional) {

            // в случаях, кгда данные не найдены, фронт хочет пустоый обьект
            SuccessResponse<SuccessDataObject<?>> successResponse = new SuccessResponse<>();

            if (((Optional<?>) body).isEmpty()) {
                SuccessDataObject<Object> successData = new SuccessDataObject<>(Map.of());
                successResponse.setData(successData);
            } else {
                SuccessDataObject<Object> successData = new SuccessDataObject<>(((Optional<?>) body).get());
                successResponse.setData(successData);
            }

            apiResponse = successResponse;
        } else {
            SuccessResponse<SuccessDataObject<?>> successResponse = new SuccessResponse<>();
            SuccessDataObject<Object> successData = new SuccessDataObject<>(body);
            successResponse.setData(successData);

            apiResponse = successResponse;
        }
        return apiResponse;
    }

}
