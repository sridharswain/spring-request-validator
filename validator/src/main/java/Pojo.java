import annotation.*;

import java.util.Set;

/**
 * @author sridharswain
 */
public class Pojo {

    @NotNull
    @RegexMatching("helo")
    private String str;

    @IntRange(lowerBound = 0, upperBound = 32)
    private int integer;

    @FloatRange(lowerBound = 0, upperBound = 24)
    private Float aFloat;

    @NotNull
    @NotEmpty
    private Set<String> nullCheckCollection = null;

    @NotEmpty
    int[] emptyCheckArray;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public Float getAFloat() {
        return aFloat;
    }

    public void setAFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public Set<String> getNullCheckCollection() {
        return nullCheckCollection;
    }

    public void setNullCheckCollection(Set<String> nullCheckCollection) {
        this.nullCheckCollection = nullCheckCollection;
    }

    public int[] getEmptyCheckArray() {
        return emptyCheckArray;
    }

    public void setEmptyCheckArray(int[] emptyCheckArray) {
        this.emptyCheckArray = emptyCheckArray;
    }
}
