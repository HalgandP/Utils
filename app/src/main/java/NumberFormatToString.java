import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

// This utils class is used to format a number like 5600 to 5,6k
public class NumberFormatToString {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE)
        {
            return format(Long.MIN_VALUE + 1);
        }
        // Negative value, should not be used
        if (value < 0)
        {
            return "-" + format(-value);
        }
        // Value doesn't need to be truncated
        if (value < 1000)
        {
            return Long.toString(value); //deal with easy case
        }

        // Retrieve the suffix to put at the end of the string, and the divideBy value to trunc the value
        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        if (hasDecimal) {
            return (truncated / 10d) + suffix;
        }
        else {
            return (truncated / 10) + suffix;
        }
    }

}