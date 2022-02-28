package uz.ksinn.utils.advice.response.wrapper;

import java.time.LocalDateTime;


public class FailureResponse<T> extends ApiResponse {

    private Data<T> data;

    public FailureResponse() {
        super(ApiResponse.STATUS_FAILURE);
    }

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }

    public static class Data<U> {

        private String code;
        private U validationCodes;
        private String message;
        private LocalDateTime timestamp;
        private String exception;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public U getValidationCodes() {
            return validationCodes;
        }

        public void setValidationCodes(U validationCodes) {
            this.validationCodes = validationCodes;
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
