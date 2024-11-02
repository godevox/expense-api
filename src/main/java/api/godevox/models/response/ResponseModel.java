package api.godevox.models.response;

import api.godevox.models.error.ErrorModel;

public class ResponseModel<T> {
    private final String status;

    private final T data;
    private final ErrorModel error;

    private ResponseModel(Builder<T> builder) {
        this.status = builder.status;
        this.data = builder.data;
        this.error = builder.error;
    }

    public static class Builder<T> {
        private String status;
        private T data;
        private ErrorModel error;

        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> error(ErrorModel error) {
            this.error = error;
            return this;
        }

        public ResponseModel<T> build() {
            return new ResponseModel<>(this);
        }
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public ErrorModel getError() {
        return error;
    }
}