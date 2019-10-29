package jar.shaw.tools.util;

/**
 * 简单的类型转换工具类
 *
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
public class TypeTransHelper
{
    /**
     * 将字符串转换成int
     * @param s 数字字符串
     * @return 字符串数字的值
     */
    public static int intOf(String s)
    {
        return Integer.valueOf(s);
    }

    /**
     * 用于将基本类型转换成字符串
     * @param obj int/double等基本类型
     * @return 基本类型对应的字符串
     */
    public static String stringOf(Object obj)
    {
        return obj.toString();
    }

    public static String byteArray2HexString(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }

    public static String byte2HexString(byte aByte) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder ret = new StringBuilder(2);
        ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
        ret.append(HEX_DIGITS[aByte & 0x0f]);
        return ret.toString();
    }
}
