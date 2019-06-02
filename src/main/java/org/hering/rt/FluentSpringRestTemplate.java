package org.hering.rt;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.function.Function;

public class FluentSpringRestTemplate<T> implements FluentRestTemplate<T> {
    private RestTemplate restTemplate;
    private Function<String, T> responseMapper;

    public FluentSpringRestTemplate() {
        this(new RestTemplate(), e -> (T) e);
    }

    public FluentSpringRestTemplate(RestTemplate restTemplate, Function<String, T> responseMapper) {
        this.restTemplate = restTemplate;
        this.responseMapper = responseMapper;
    }

    @Override
    public <R> RestResponse<R, T> get(String url, Class<R> responseType) {
        try {
            var response = restTemplate.getForEntity(url, responseType);
            return new SuccessResponse<>(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<R, T> get(String url, ParameterizedTypeReference<R> responseType) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(HttpMethod.GET, URI.create(url)), responseType);
            return new SuccessResponse<>(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<R, T> post(String url, Object body, Class<R> responseType) {
        try {
            var response = restTemplate.postForEntity(url, body, responseType);
            return new SuccessResponse<>(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<R, T> post(String url, Object body, ParameterizedTypeReference<R> responseType) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(body, HttpMethod.POST, URI.create(url)), responseType);
            return new SuccessResponse<>(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<Void, T> put(String url, Object body, Class<R> responseType) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(body, HttpMethod.PUT, URI.create(url)), responseType);
            return new SuccessResponse<>(response.getStatusCode(), null);
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<Void, T> put(String url, Object body, ParameterizedTypeReference<R> responseType) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(body, HttpMethod.PUT, URI.create(url)), responseType);
            return new SuccessResponse<>(response.getStatusCode(), null);
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<R, T> patch(String url, Object body, Class<R> responseType) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(body, HttpMethod.PATCH, URI.create(url)), responseType);
            return new SuccessResponse<>(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <R> RestResponse<R, T> patch(String url, Object body, ParameterizedTypeReference<R> responseType) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(body, HttpMethod.PATCH, URI.create(url)), responseType);
            return new SuccessResponse<>(response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }

    @Override
    public <Void> RestResponse<Void, T> delete(String url, Object body) {
        try {
            var response = restTemplate.exchange(new RequestEntity<>(body, HttpMethod.DELETE, URI.create(url)), new ParameterizedTypeReference<Void>() {
            });
            return new SuccessResponse(response.getStatusCode(), null);
        } catch (HttpStatusCodeException e) {
            return new ErrorResponse<>(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString()));
        }
    }
}