package uz.ksinn.utils.advice.response.wrapper;

public class SuccessResponse<T> extends ApiResponse {

    private T data;

    public SuccessResponse() {
        super(ApiResponse.STATUS_SUCCESS);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}



