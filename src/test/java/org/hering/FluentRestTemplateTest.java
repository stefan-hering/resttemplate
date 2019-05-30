package org.hering;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hering.rt.ApiError;
import org.hering.rt.FluentRestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class FluentRestTemplateTest {
    private FluentRestTemplate<ProblemJson> fluentRestTemplate;
    private HttpHeaders headers;
    private MockRestServiceServer server;

    @BeforeEach
    public void setup() {
        var template = new RestTemplate();
        fluentRestTemplate = new FluentRestTemplate<>(template, s -> {
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
    public void testingSuccess() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/test"))
                .andRespond(MockRestResponseCreators.withSuccess().headers(headers)
                        .body("{\"value\":\"test\"}"));
        var response = fluentRestTemplate.get("http://localhost/test", TestModel.class);

        assertThat(response.success().isPresent()).isTrue();
        assertThat(response.failure().isEmpty()).isTrue();

        assertThat(response.success(TestModel::getValue).get()).isEqualTo("test");
        assertThat(response.failure(ApiError::result).isPresent()).isFalse();

        response.onSuccess(model -> assertThat(model.getValue()).isEqualTo("test"));
        response.onFailure(error -> fail("should not be called"));
    }

    @Test
    public void testingFailure() {
        server.expect(MockRestRequestMatchers.requestTo("http://localhost/error"))
                .andRespond(MockRestResponseCreators.withBadRequest().headers(headers)
                        .body("{\"type\":\"test\"}"));

        var response = fluentRestTemplate.get("http://localhost/error", TestModel.class);

        assertThat(response.success().isEmpty()).isTrue();
        assertThat(response.failure().isPresent()).isTrue();

        assertThat(response.success(TestModel::getValue).isPresent()).isFalse();
        assertThat(response.failure(ApiError::result).map(ProblemJson::getType).get()).isEqualTo("test");

        response.onSuccess(model -> fail("should not be called"))
                .onFailure(error -> assertThat(error.result().getType()).isEqualTo("test"));
    }
}
