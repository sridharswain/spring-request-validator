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
public @interface IntRange {
    int lowerBound() default Integer.MIN_VALUE;

    int upperBound() default Integer.MAX_VALUE;

}
