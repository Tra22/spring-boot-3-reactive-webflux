package com.webflux.demo.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("fakeAPIHttpClientProperty")
@ConfigurationProperties(prefix = "client.fake.api")
public class FakeAPIHttpClientProperty {
    private String url;
}
