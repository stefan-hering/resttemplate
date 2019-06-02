package org.hering.rt;

import org.springframework.http.HttpStatus;

/**
 * Represents a response from an API call with a result that should be typed to the error format of that API.
 * @param <T>
 */
public class ApiResult<T> {
    private HttpStatus status;
    private T result;

    public ApiResult(HttpStatus status, T result) {
        this.status = status;
        this.result = result;
    }

    public HttpStatus status() {
        return status;
    }

    public T result() {
        return result;
    }
}
