import java.util.HashSet;
import java.util.Set;

/**
 * @author sridharswain
 */
public class Main {
    public static void main(String... args) {
        Pojo pojo = new Pojo();
        pojo.setInteger(34);
        pojo.setStr("heloo");
        pojo.setAFloat(25F);
        Set<String> dummyArray = new HashSet<>();
        dummyArray.add("asdf");
        pojo.setNullCheckCollection(dummyArray);
        int[] x = new int[2];
        pojo.setEmptyCheckArray(x);
        Validator.validate(pojo);
    }
}
