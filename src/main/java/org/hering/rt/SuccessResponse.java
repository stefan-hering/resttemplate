package org.hering.rt;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SuccessResponse <T, P> implements RestResponse<T, P> {
    private T result;

    protected  SuccessResponse(T result) {
        this.result = result;
    }

    @Override
    public Optional<T> success() {
        return Optional.of(result);
    }

    @Override
    public Optional<ApiError<P>> failure() {
        return Optional.empty();
    }

    @Override
    public <Q> Optional<Q> success(Function<T, Q> f) {
        return Optional.ofNullable(f.apply(result));
    }

    @Override
    public <Q> Optional<Q> failure(Function<ApiError<P>, Q> f) {
        return Optional.empty();
    }

    @Override
    public RestResponse<T, P> onSuccess(Consumer<T> f) {
        f.accept(result);
        return this;
    }

    @Override
    public RestResponse<T, P> onFailure(Consumer<ApiError<P>> f) {
        return this;
    }
}
