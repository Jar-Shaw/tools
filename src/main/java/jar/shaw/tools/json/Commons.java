package jar.shaw.tools.json;

import java.util.regex.Pattern;

import static jar.shaw.tools.util.Utils.isEmpty;

/**
 * Json相关的一些公共方法
 * @author 肖佳
 * @since 1.8
 * 创建时间：2018/11/24
 */
public class Commons
{
    /**
     * 将json path的多个部分拼接成path字符串
     * @param paths 如果参数不是纯数字，则会用点与前面的path拼接起来；如果某个参数是存数字，则会当成数组下标，用中括号括起来添加到前面的path中
     *              例如jsonPath("a",0,"b")为a[0].b
     * @return 拼接好的path
     */
    public static String jsonPath(Object ...paths)
    {
        Pattern pattern = Pattern.compile("^\\d+$");
        StringBuilder sb = new StringBuilder();
        for (Object o : paths)
        {
            if (isEmpty(o))
            {
                continue;
            }
            String s = o.toString().trim();
            if (pattern.matcher(s).find())
            {
                sb.append("[").append(s).append("]");
            } else {
                sb.append(".").append(s);
            }
        }
        return sb.deleteCharAt(0).toString();
    }

    /**
     * 移除 json path 中的所有数组下标
     * @param path json path
     * @return 不包含数组下标的 json path
     */
    public static String removeAllIndex(String path) {
        return path.replaceAll("\\[\\d+]", "");
    }

}
