package uz.ksinn.utils.advice.response.wrapper;

public class SuccessDataObject<T> {

    private T object;

    public SuccessDataObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
