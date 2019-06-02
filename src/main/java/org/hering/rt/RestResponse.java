package org.hering.rt;

import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A response from a http call.
 * @param <T> The response type defined per http call
 * @param <P> The error type defined once per RestTemplate
 */
public interface RestResponse<T, P> {
    /**
     * Just the status of the call. If Java had a good type system, there would be a method called result that is T or P.
     */
    HttpStatus status();

    /**
     * @return <code>true</code> if the restcall was answered with 2xx success
     *         <code>false</code> otherwise
     */
    boolean wasSuccessful();
    /**
     * @return <code>true</code> if the restcall was answered with 4xx or 5xx code
     *         <code>false</code> otherwise
     */
    boolean hasFailed();

    Optional<ApiResult<T>> success();
    Optional<ApiResult<P>> failure();

    <Q> Optional<Q> success(Function<ApiResult<T>, Q> f);
    <Q> Optional<Q> failure(Function<ApiResult<P>, Q> f);

    RestResponse<T, P> onSuccess(Consumer<ApiResult<T>> f);
    RestResponse<T, P> onFailure(Consumer<ApiResult<P>> f);
    <E extends Throwable> RestResponse<T, P> onFailureThrow(Function<ApiResult<P>, E> s) throws E;
    <E extends Throwable> RestResponse<T, P> onFailureThrowDefault(Supplier<E> s) throws E;
}
