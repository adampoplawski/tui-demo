package poplawski.adam.tuidemo.exceptions;

public class GitRequestLimitsExceeded extends RuntimeException {
    public GitRequestLimitsExceeded(String message) {
        super(message);
    }
}
