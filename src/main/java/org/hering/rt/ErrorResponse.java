package org.hering.rt;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ErrorResponse<T, P> implements RestResponse<T, P> {
    private final HttpStatus status;
    private final P result;

    protected  ErrorResponse(HttpStatus status, P result) {
        this.status = status;
        this.result = result;
    }


    @Override
    public HttpStatus status() {
        return status;
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public boolean hasFailed() {
        return true;
    }

    @Override
    public Optional<ApiResult<T>> success() {
        return Optional.empty();
    }

    @Override
    public Optional<ApiResult<P>> failure() {
        return Optional.of(new ApiResult<>(status, result));
    }

    @Override
    public <Q> Optional<Q> success(Function<ApiResult<T>, Q> f) {
        return Optional.empty();
    }

    @Override
    public <Q> Optional<Q> failure(Function<ApiResult<P>, Q> f) {
        return Optional.of(f.apply(new ApiResult<>(status, result)));
    }

    @Override
    public RestResponse<T, P> onSuccess(Consumer<ApiResult<T>> f) {
        return this;
    }

    @Override
    public RestResponse<T, P> onFailure(Consumer<ApiResult<P>> f) {
        f.accept(new ApiResult<>(status, result));
        return this;
    }

    @Override
    public <E extends Throwable> RestResponse<T, P> onFailureThrow(Function<ApiResult<P>, E> f) throws E {
        throw f.apply(new ApiResult<>(status, result));
    }

    @Override
    public <E extends Throwable> RestResponse<T, P> onFailureThrowDefault(Supplier<E> s) throws E {
        throw s.get();
    }
}
