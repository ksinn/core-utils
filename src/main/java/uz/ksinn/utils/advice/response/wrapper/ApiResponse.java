package uz.ksinn.utils.advice.response.wrapper;

import java.io.Serializable;

public class ApiResponse implements Serializable {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILURE = "failure";

    private String status;

    public ApiResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
