package com.webflux.demo.configurations;

import com.webflux.demo.configurations.properties.FakeAPIHttpClientProperty;
import com.webflux.demo.exceptions.InternalHttpClientException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ClientAPIConfiguration {

    private static final String REQUEST_CONTEXT_KEY = "request";
    private final FakeAPIHttpClientProperty fakeAPIHttpClientProperty;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10))
                        .addHandlerLast(new WriteTimeoutHandler(10))
                );
    }

    @Bean
    @Primary
    public WebClient defaultWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .build();
    }

    @Bean("fakeAPIWebClient")
    public WebClient fakeAPIWebClient() {
        return WebClient.builder()
                .baseUrl(fakeAPIHttpClientProperty.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader("User-Agent", "MyApp/1.0")
                .filter(enrichContextWithRequest())
                .filter(logRequest())
                .filter(addTraceIdToContext())
                .filter(logResponse())
                .build();
    }

    private ExchangeFilterFunction enrichContextWithRequest() {
        return (request, next) -> next.exchange(request)
                .contextWrite(ctx -> ctx.put(REQUEST_CONTEXT_KEY, request));
    }

    private ExchangeFilterFunction addTraceIdToContext() {
        return (request, next) -> {
            String traceId = MDC.get("traceId");
            return next.exchange(request).contextWrite(ctx -> ctx.put("traceId", traceId));
        };
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest ->
                Mono.deferContextual(ctx -> {
                    StringBuilder sb = new StringBuilder("Request:\n");
                    sb.append(clientRequest.method()).append(" ").append(clientRequest.url()).append("\n");

                    clientRequest.headers().forEach((key, values) -> {
                        sb.append(key).append(": ").append(String.join(",", values)).append("\n");
                    });

                    log.info(sb.toString());
                    return Mono.just(clientRequest);
                })
        );
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(originalResponse ->
                Mono.deferContextual(ctxView -> {
                    String traceId = ctxView.getOrEmpty("traceId")
                            .map(Object::toString)
                            .orElse("N/A");

                    MDC.put("traceId", traceId);

                    return originalResponse.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(body -> {
                                try {
                                    log.info("Response Status: {}", originalResponse.statusCode());
                                    log.info("Response Headers: {}",
                                            originalResponse.headers().asHttpHeaders()
                                                    .entrySet()
                                                    .stream()
                                                    .map(entry -> entry.getKey() + ": " + String.join(",", entry.getValue()))
                                                    .collect(Collectors.joining(" | "))
                                    );

                                    String contentType = originalResponse.headers()
                                            .contentType()
                                            .map(MediaType::toString)
                                            .orElse("");

                                    boolean isTextual = contentType.startsWith("text/") || contentType.contains("json") || contentType.contains("xml");

                                    if (!isTextual) {
                                        log.warn("Non-text response (content-type: {}). Skipping body logging.", contentType);
                                        return Mono.just(originalResponse);
                                    }

                                    if (originalResponse.statusCode().isError() || body.isEmpty()) {
                                        log.error("Response Error Body: {}", body);
                                        String url = "UNKNOWN";
                                        String requestInfo = "EMPTY REQUEST!";
                                        ClientRequest req = ctxView.getOrDefault(REQUEST_CONTEXT_KEY, null);
                                        if (req != null) {
                                            url = String.valueOf(req.url());
                                            requestInfo = formatRequest(req);
                                        }
                                        return Mono.error(new InternalHttpClientException(body, originalResponse.statusCode().value(), url, requestInfo, body));
                                    } else {
                                        log.info("Response Body: {}", body);
                                    }

                                    ClientResponse rebuilt = ClientResponse.create(originalResponse.statusCode())
                                            .headers(headers -> headers.addAll(originalResponse.headers().asHttpHeaders()))
                                            .body(body)
                                            .build();

                                    return Mono.just(rebuilt);
                                } finally {
                                    MDC.remove("traceId"); // Clean up
                                }
                            });
                })
        );
    }

    private static String formatRequest(ClientRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.method()).append(" ").append(request.url()).append("\nHeaders: ");
        for (Map.Entry<String, List<String>> header : request.headers().entrySet()) {
            sb.append(header.getKey()).append("=").append(String.join(",", header.getValue())).append("; ");
        }
        return sb.toString();
    }
}
