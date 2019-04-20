package exception;

import java.lang.annotation.Annotation;

/**
 * @author sridharswain
 */
public class ValidationNotApplicable extends RuntimeException {
    public ValidationNotApplicable(Class<? extends Annotation> annotationType, Class fieldType) {
        super(annotationType.getName().concat(" is not applicable on ").concat(fieldType.getName()));
    }
}
