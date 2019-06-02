package org.hering.rt;

import org.springframework.core.ParameterizedTypeReference;

public interface FluentRestTemplate<T> {
    <R> RestResponse<R, T> get(String url, Class<R> responseType);

    <R> RestResponse<R, T> get(String url, ParameterizedTypeReference<R> responseType);

    <R> RestResponse<R, T> post(String url, Object body, Class<R> responseType);

    <R> RestResponse<R, T> post(String url, Object body, ParameterizedTypeReference<R> responseType);

    <R> RestResponse<Void, T> put(String url, Object body, Class<R> responseType);

    <R> RestResponse<Void, T> put(String url, Object body, ParameterizedTypeReference<R> responseType);

    <R> RestResponse<R, T> patch(String url, Object body, Class<R> responseType);

    <R> RestResponse<R, T> patch(String url, Object body, ParameterizedTypeReference<R> responseType);

    <Void> RestResponse<Void, T> delete(String url, Object body);
}
