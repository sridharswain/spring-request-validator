import annotation.*;
import exception.GetterNotFoundException;
import exception.ValidationNotApplicable;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sridharswain
 */
public class Validator {
    private static Map<Class<? extends Annotation>, List<Class<?>>> applicableMap;

    static {
        applicableMap = new HashMap<>();

        applicableMap.put(DoubleRange.class, new ArrayList<Class<?>>() {{
            add(double.class);
            add(Double.class);
        }});

        applicableMap.put(IntRange.class, new ArrayList<Class<?>>() {{
            add(int.class);
            add(Integer.class);
        }});

        applicableMap.put(FloatRange.class, new ArrayList<Class<?>>() {{
            add(float.class);
            add(Float.class);
        }});

        applicableMap.put(NotEmpty.class, new ArrayList<Class<?>>() {{
            add(Collection.class);
            add(String.class);
            add(Object[].class);
        }});

        applicableMap.put(RegexMatching.class, new ArrayList<Class<?>>() {{
            add(String.class);
        }});

        applicableMap.put(NotBlank.class, new ArrayList<Class<?>>() {{
            add(String.class);
        }});
    }

    private Validator() {
    }

    public static <T> void validate(T object) {
        Class type = object.getClass();

        Field[] fields = type.getDeclaredFields();
        ArrayList<String> errors = new ArrayList<>();
        for (Field field : fields) {
            Object value = getValueOf(field, object);
            Class fieldType = field.getType();

            if (field.isAnnotationPresent(DoubleRange.class)) {
                validateApplicable(DoubleRange.class, fieldType);
                errors.add(validateDoubleRange(field, (Double) value));
            }

            if (field.isAnnotationPresent(IntRange.class)) {
                validateApplicable(IntRange.class, fieldType);
                errors.add(validateIntRange(field, (Integer) value));
            }

            if (field.isAnnotationPresent(FloatRange.class)) {
                validateApplicable(FloatRange.class, fieldType);
                errors.add(validateFloatRange(field, (Float) value));
            }

            if (field.isAnnotationPresent(NotNull.class)) {
                validateApplicable(NotNull.class, fieldType);
                errors.add(validateNotNull(field, value));
            }

            if (field.isAnnotationPresent(NotEmpty.class)) {
                validateApplicable(NotEmpty.class, fieldType);
                errors.add(validateNotEmpty(field, value));
            }

            if (field.isAnnotationPresent(NotBlank.class)) {
                validateApplicable(NotBlank.class, fieldType);
                errors.add(validateNotEmpty(field, ((String) value).trim()));
            }

            if (field.isAnnotationPresent(RegexMatching.class)) {
                validateApplicable(RegexMatching.class, fieldType);
                errors.add(validateRegex(field, (String) value));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        errors.stream()
                .filter(message -> !message.isEmpty())
                .forEach(message -> stringBuilder.append(message).append("\n"));
        System.out.println(stringBuilder.toString().trim());
    }

    private static <T> Object getValueOf(Field field, T object) {
        Class type = object.getClass();
        String fieldName = field.getName();
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
            return propertyDescriptor.getReadMethod().invoke(object);
        } catch (Exception e) {
            throw new GetterNotFoundException(fieldName);
        }
    }

    private static void validateApplicable(Class<? extends Annotation> annotation, Class<?> fieldType) {
        List<Class<?>> acceptableTypes = applicableMap.get(annotation);
        if (acceptableTypes == null) {
            // All Annotations are supported
            return;
        }

        for (Class<?> acceptableType : acceptableTypes) {
            if (acceptableType.isAssignableFrom(fieldType)) {
                return;
            }
        }

        // Explicit check for array
        if (annotation == NotEmpty.class && fieldType.isArray()) {
            return;
        }
        throw new ValidationNotApplicable(annotation, fieldType);
    }

    private static String validateDoubleRange(Field field, Double value) {
        DoubleRange doubleRange = field.getAnnotation(DoubleRange.class);
        double lowerBound = doubleRange.lowerBound();
        double upperBound = doubleRange.upperBound();

        if (doubleRange == null) {
            return "";
        }
        return (value >= lowerBound && value <= upperBound)
                ? ""
                : field.getName().concat(" should be in range "
                .concat(String.valueOf(lowerBound))
                .concat(" and ")
                .concat(String.valueOf(upperBound)));
    }

    private static String validateIntRange(Field field, Integer value) {
        IntRange intRange = field.getAnnotation(IntRange.class);
        int lowerBound = intRange.lowerBound();
        int upperBound = intRange.upperBound();

        if (intRange == null) {
            return "";
        }
        return (value >= lowerBound && value <= upperBound)
                ? ""
                : field.getName().concat(" should be in range "
                .concat(String.valueOf(lowerBound))
                .concat(" and ")
                .concat(String.valueOf(upperBound)));
    }

    private static String validateFloatRange(Field field, Float value) {
        FloatRange floatRange = field.getAnnotation(FloatRange.class);
        float lowerBound = floatRange.lowerBound();
        float upperBound = floatRange.upperBound();

        if (floatRange == null) {
            return "";
        }
        return (value >= lowerBound && value <= upperBound)
                ? ""
                : field.getName().concat(" should be in range "
                .concat(String.valueOf(lowerBound))
                .concat(" and ")
                .concat(String.valueOf(upperBound)));
    }

    private static String validateNotNull(Field field, Object value) {
        return (value == null)
                ? field.getName().concat(" cannot be null")
                : "";
    }

    private static String validateNotEmpty(Field field, Object value) {
        if (value == null) {
            return "";
        }
        int length = 0;
        Class fieldType = field.getType();
        if (value instanceof Collection<?>) {
            length = ((Collection<?>) value).size();
        } else if (value instanceof Map<?, ?>) {
            length = ((Map<?, ?>) value).size();
        } else if (value instanceof String) {
            length = ((String) value).length();
        } else if (fieldType.isArray()) {
            length = Array.getLength(value);
        }

        return (length == 0)
                ? field.getName().concat(" cannot be empty")
                : "";
    }

    private static String validateRegex(Field field, String value) {
        RegexMatching regexMatching = field.getAnnotation(RegexMatching.class);
        String regex = regexMatching.value();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return (!matcher.matches())
                ? field.getName().concat(" does not match pattern.")
                : "";
    }
}
