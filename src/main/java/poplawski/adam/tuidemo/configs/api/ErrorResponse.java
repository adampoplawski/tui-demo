package poplawski.adam.tuidemo.configs.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private final int status;
    private final String message;
}