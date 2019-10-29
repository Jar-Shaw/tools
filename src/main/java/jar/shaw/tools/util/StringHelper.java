package jar.shaw.tools.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 肖佳
 * @since 1.8
 * 创建时间：2018/11/21
 */
public class StringHelper
{
    /**
     * <p>用分隔符连接字符串</p>
     * <p>调用传入对象的toString方法然后用分隔符连接</p>
     * @param delimiter 分隔符
     * @param objects 要连接的字符串
     * @return 用分隔符连接的字符串
     */
    public static String concat(String delimiter, Object... objects)
    {
        return Stream.of(objects).map(Object::toString).collect(Collectors.joining(delimiter));
    }
}
