package poplawski.adam.tuidemo.exceptions;

public class GitUserNotFoundException extends RuntimeException {
    public GitUserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}