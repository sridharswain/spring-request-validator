package exception;

/**
 * @author sridharswain
 */
public class GetterNotFoundException extends RuntimeException {
    public GetterNotFoundException(String fieldName) {
        super("No getter for field ".concat(fieldName));
    }
}
