package org.hering;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hering.rt.ApiResult;
import org.hering.rt.FluentRestTemplate;
import org.hering.rt.FluentSpringRestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

/**
 * Simple test to cover basic cases
 */
public class FluentRestTemplateTest {
    private FluentRestTemplate<ProblemJson> fluentRestTemplate;
    private HttpHeaders headers;
    private MockRestServiceServer server;

    @BeforeEach
    public void setup() {
        var template = new RestTemplate();
        fluentRestTemplate = new FluentSpringRestTemplate<>(template, s -> {
            try {
                return new ObjectMapper().readValue(s, ProblemJson.class);
            } catch (IOException e) {
                return null;
            }
        });

        headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        server = MockRestServiceServer.bindTo(template).build();
    }

    @Test
    public void testGetSuccess() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/test"))
                .andRespond(MockRestResponseCreators.withSuccess().headers(headers)
                        .body("{\"value\":\"test\"}"));
        var response = fluentRestTemplate.get("http://localhost/test", TestModel.class);

        assertThat(response.wasSuccessful()).isTrue();
        assertThat(response.hasFailed()).isFalse();

        assertThat(response.success().isPresent()).isTrue();
        assertThat(response.failure().isEmpty()).isTrue();

        assertThat(response.success(ApiResult::result).get().getValue()).isEqualTo("test");
        assertThat(response.failure(ApiResult::result).isPresent()).isFalse();

        response.onSuccess(model -> assertThat(model.result().getValue()).isEqualTo("test"))
                .onFailure(error -> fail("should not be called"));

        assertThatCode(() -> response.onFailureThrow(error -> new NullPointerException()))
                .doesNotThrowAnyException();
        assertThatCode(() -> response.onFailureThrowDefault(NullPointerException::new))
                .doesNotThrowAnyException();
    }

    @Test
    public void testGetFailure() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/error"))
                .andRespond(MockRestResponseCreators.withBadRequest().headers(headers)
                        .body("{\"type\":\"test\"}"));

        var response = fluentRestTemplate.get("http://localhost/error", TestModel.class);

        assertThat(response.wasSuccessful()).isFalse();
        assertThat(response.hasFailed()).isTrue();

        assertThat(response.success().isEmpty()).isTrue();
        assertThat(response.failure().isPresent()).isTrue();

        assertThat(response.success().isPresent()).isFalse();
        assertThat(response.failure(ApiResult::result).map(ProblemJson::getType).get()).isEqualTo("test");

        response.onSuccess(model -> fail("should not be called"))
                .onFailure(error -> assertThat(error.result().getType()).isEqualTo("test"));

        assertThatThrownBy(() -> response.onFailureThrow(error -> new NullPointerException()));
        assertThatThrownBy(() -> response.onFailureThrowDefault(NullPointerException::new));
    }

    @Test
    public void testPostSuccess() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/test"))
                .andRespond(MockRestResponseCreators.withSuccess().headers(headers)
                        .body("{\"value\":\"test\"}"));
        var response = fluentRestTemplate.post("http://localhost/test", null, TestModel.class);

        assertThat(response.success(ApiResult::result).get().getValue()).isEqualTo("test");
    }

    @Test
    public void testPostFailure() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/error"))
                .andRespond(MockRestResponseCreators.withBadRequest().headers(headers)
                        .body("{\"type\":\"test\"}"));

        var response = fluentRestTemplate.post("http://localhost/error", null, TestModel.class);

        assertThat(response.failure().isPresent()).isTrue();
    }
}
