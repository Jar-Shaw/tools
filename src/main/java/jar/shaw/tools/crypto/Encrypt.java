package jar.shaw.tools.crypto;

import java.security.MessageDigest;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2018/12/6</p>
 */
public class Encrypt {

    /**
     * 计算给定字符串的MD5值
     * @param s 要计算的字符串
     * @return 字符串的MD5值
     */
    public static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
            StringBuilder ret = new StringBuilder(bytes.length * 2);
            for (byte aByte : bytes) {
                ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
                ret.append(HEX_DIGITS[aByte & 0x0f]);
            }
            return ret.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
