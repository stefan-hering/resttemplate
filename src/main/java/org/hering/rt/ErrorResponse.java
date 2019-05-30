package org.hering.rt;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ErrorResponse<T, P> implements RestResponse<T, P> {
    private ApiError<P> result;

    protected ErrorResponse(ApiError result) {
        this.result = result;
    }

    @Override
    public Optional<T> success() {
        return Optional.empty();
    }

    @Override
    public Optional<ApiError<P>> failure() {
        return Optional.of(result);
    }

    @Override
    public <Q> Optional<Q> success(Function<T, Q> f) {
        return Optional.empty();
    }

    @Override
    public <Q> Optional<Q> failure(Function<ApiError<P>, Q> f) {
        return Optional.of(f.apply(result));
    }

    @Override
    public RestResponse<T, P> onSuccess(Consumer<T> f) {
        return this;
    }

    @Override
    public RestResponse<T, P> onFailure(Consumer<ApiError<P>> f) {
        f.accept(result);
        return this;
    }
}
