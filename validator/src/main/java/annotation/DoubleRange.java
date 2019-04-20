package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sridharswain
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DoubleRange {
    double lowerBound() default Double.MIN_VALUE;

    double upperBound() default Double.MAX_VALUE;

}
