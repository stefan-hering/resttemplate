package org.hering.rt;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

public class FluentRestTemplate <T> {
    private RestTemplate restTemplate;
    private Function<String, T> responseMapper;

    public FluentRestTemplate() {
        this(new RestTemplate(), e -> (T) e);
    }

    public FluentRestTemplate(RestTemplate restTemplate, Function<String, T> responseMapper) {
        this.restTemplate = restTemplate;
        this.responseMapper = responseMapper;
    }

    public <R> RestResponse<R, T> get(String url, Class<R> responseType){
        try {
            var response = restTemplate.getForEntity(url, responseType);
            return new SuccessResponse<>(response.getBody());
        } catch(HttpStatusCodeException e) {
            return new ErrorResponse<>(new ApiError(e.getStatusCode(), responseMapper.apply(e.getResponseBodyAsString())));
        }
    }
}