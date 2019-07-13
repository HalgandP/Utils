import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// This class is used to encrypt or decrypt String (like password etc)
public class EncrypterDecrypter {

    /**
     * Returns the SHA-512 hashcode of the given string.
     * @param s the string to be hashed.
     * @return the SHA-512 hashcode of {@code s}.
     */
    public static String encryptToSha512(String s) throws ExceptionUtil {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(s.getBytes());
        byte[] bytes = md.digest();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
            buffer.append(tmp);
        }
        return buffer.toString();
    }

    // Compare two Strings (one already encrypted, the second not encrypted)
    public static boolean compare(String shaString, String s2) throws NoSuchAlgorithmException {

        byte[] bytes1 = shaString.getBytes();

        try {
            // Encrypt to sha512 the second string
            s2 = encryptToSha512(s2);
        } catch (ExceptionUtil e) {
            e.printStackTrace();
        }

        // Check if the two encrypted strings are equal and return the result
        return MessageDigest.isEqual(bytes1, s2.getBytes());
    }
}
