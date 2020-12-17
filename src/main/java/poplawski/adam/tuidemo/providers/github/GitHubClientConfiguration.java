package poplawski.adam.tuidemo.providers.github;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poplawski.adam.tuidemo.exceptions.GitIntegrationException;
import poplawski.adam.tuidemo.exceptions.GitRequestLimitsExceeded;
import poplawski.adam.tuidemo.exceptions.GitUserNotFoundException;

@Configuration
public class GitHubClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}

class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 404 -> new GitUserNotFoundException("Git user not found");
            case 403 -> new GitRequestLimitsExceeded("Git request limits exceeded");
            default -> new GitIntegrationException("Unhandled Git integration error");
        };
    }
}
