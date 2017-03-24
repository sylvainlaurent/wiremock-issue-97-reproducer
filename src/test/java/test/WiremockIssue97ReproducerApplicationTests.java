package test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WiremockIssue97ReproducerApplicationTests {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
    
    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setupWireMock() {
        stubFor(//
                get(urlEqualTo("/test"))//
                        .willReturn(aResponse().withStatus(200)//
                                .withHeader("Content-Type", "application/json")//
                                .withBody("{ \"hello\": \"get\" }")));
        stubFor(//
                post(urlEqualTo("/test"))//
                        .willReturn(aResponse().withStatus(200)//
                                .withHeader("Content-Type", "application/json")//
                                .withFixedDelay(0)//
                                .withBody("{ \"hello\": \"post\" }")));

    }

    @Test
    @Repeat(100)
    public void test() {
        restTemplate.getForObject("http://localhost:8089/test", String.class);
    }

}
