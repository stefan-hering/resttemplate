package org.hering.rt;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface RestResponse<T, P> {
    Optional<T> success();
    Optional<ApiError<P>> failure();

    <Q> Optional<Q> success(Function<T, Q> f);
    <Q> Optional<Q> failure(Function<ApiError<P>, Q> f);

    RestResponse<T, P> onSuccess(Consumer<T> f);
    RestResponse<T, P> onFailure(Consumer<ApiError<P>> f);
}
