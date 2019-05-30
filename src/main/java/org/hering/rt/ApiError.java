package org.hering.rt;

import org.springframework.http.HttpStatus;

public class ApiError <T> {
    private HttpStatus statusCode;
    private T result;

    public ApiError(HttpStatus statusCode, T result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public HttpStatus statusCode() {
        return statusCode;
    }

    public T result() {
        return result;
    }
}
