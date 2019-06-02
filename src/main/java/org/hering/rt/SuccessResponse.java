package org.hering.rt;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SuccessResponse <T, P> implements RestResponse<T, P> {
    private final HttpStatus status;
    private final T result;

    protected  SuccessResponse(HttpStatus status, T result) {
        this.status = status;
        this.result = result;
    }

    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public boolean wasSuccessful() {
        return true;
    }

    @Override
    public boolean hasFailed() {
        return false;
    }

    @Override
    public Optional<ApiResult<T>> success() {
        return Optional.of(new ApiResult<>(status, result));
    }

    @Override
    public Optional<ApiResult<P>> failure() {
        return Optional.empty();
    }

    @Override
    public <Q> Optional<Q> success(Function<ApiResult<T>, Q> f) {
        return Optional.ofNullable(f.apply(new ApiResult<>(status, result)));
    }

    @Override
    public <Q> Optional<Q> failure(Function<ApiResult<P>, Q> f) {
        return Optional.empty();
    }

    @Override
    public RestResponse<T, P> onSuccess(Consumer<ApiResult<T>> f) {
        f.accept(new ApiResult<>(status, result));
        return this;
    }

    @Override
    public RestResponse<T, P> onFailure(Consumer<ApiResult<P>> f) {
        return this;
    }

    @Override
    public <E extends Throwable> RestResponse<T, P> onFailureThrow(Function<ApiResult<P>, E> s) throws E {
        return this;
    }

    @Override
    public <E extends Throwable> RestResponse<T, P> onFailureThrowDefault(Supplier<E> s) throws E {
        return this;
    }

}
