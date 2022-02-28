package uz.ksinn.utils.advice.response.wrapper;

import java.time.LocalDateTime;

public class ApiResponseError {

    private String status;
    private Body data;

    public String getStatus() {
        return status;
    }

    public Body getData() {
        return data;
    }

    public void setData(Body data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Body {

        private String code;
        private String message;
        private LocalDateTime timestamp;
        private String exception;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }
    }
}
