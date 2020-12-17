package poplawski.adam.tuidemo.exceptions;

public class GitIntegrationException extends RuntimeException {
    public GitIntegrationException(String errorMessage) {
        super(errorMessage);
    }
}
