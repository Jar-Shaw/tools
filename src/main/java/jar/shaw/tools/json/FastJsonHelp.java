package jar.shaw.tools.json;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;

import static jar.shaw.tools.util.Utils.isEmpty;

/**
 * FastJson的帮助类，可以快速获取指定path需要的值
 * 2019/8/30日更新为采用 JSONPath
 * @author 肖佳
 * @since 1.8
 * 创建时间：2018/11/24
 */
public class FastJsonHelp
{

    public static final int FASTJSON_PARSE_FEATURE;
    public static final int FASTJSON_GENERATE_FEATURE;

    static
    {
        int feature = JSON.DEFAULT_PARSER_FEATURE;
        feature &= ~Feature.UseBigDecimal.getMask();
        feature |= Feature.OrderedField.getMask();
        FASTJSON_PARSE_FEATURE = feature;

        feature = JSON.DEFAULT_GENERATE_FEATURE;
        feature |= Feature.DisableCircularReferenceDetect.getMask();
        FASTJSON_GENERATE_FEATURE = feature;
    }

    /**
     * fastjson 默认将小数解析成 bigdecimal 类型， 这里去掉了该特性，近根据自定义的特性解析
     * @param json json 字符串
     * @return 解析的 JSONObject
     */
    public static FastJsonEnhancer parseObject(String json)
    {
        return FastJsonEnhancer.enhance((JSONObject) JSON.parse(json, FASTJSON_PARSE_FEATURE));
    }

    /**
     * fastjson 默认将小数解析成 bigdecimal 类型， 这里去掉了该特性，近根据自定义的特性解析
     * @param json json 字符串
     * @return 解析的 JSONArray
     */
    public static JSONArray parseArray(String json)
    {
        return (JSONArray) JSON.parse(json, FASTJSON_PARSE_FEATURE);
    }

    /**
     * 根据设置的默认配置将 JSON 序列化
     * @param json JSON 对象
     * @return 序列化后的字符串
     */
    public static String toJSONString(JSON json)
    {
        return JSON.toJSONString(json, FASTJSON_GENERATE_FEATURE);
    }

    /**
     * 获取path对应的 JSONArray 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 JSONArray 值，如果属性不存在，返回null
     */
    public static JSONArray getJSONArray(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? new JSONArray() : (JSONArray) value;
    }

    /**
     * 获取path对应的 JSONObject 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 JSONObject 值，如果属性不存在，返回null
     */
    public static JSONObject getJSONObject(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? new JSONObject() : (JSONObject) value;
    }

    /**
     * 获取path对应的 String 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return 获取path对应的 String 值，如果属性不存在，返回null
     */
    public static String getJsonString(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? null : value.toString();
    }

    /**
     * 获取path对应的 Integer 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 Integer 值，如果属性不存在，返回null
     */
    public static Integer getJsonInt(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? null : (Integer)value;
    }

    /**
     * 获取path对应的 Float 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 Float 值，如果属性不存在，返回null
     */
    public static Float getJsonFloat(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? null : (Float)value;

    }

    /**
     * 获取path对应的 Double 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 Double 值，如果属性不存在，返回null
     */
    public static Double getJsonDouble(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? null : (Double) value;

    }

    /**
     * 获取path对应的 Boolean 值
     * @param jsonObject 操作的JSONObject
     * @param path 能唯一确定一个值的path
     * @return 获取path对应的 Boolean 值，如果属性不存在，返回null
     */
    public static Boolean getJsonBoolean(JSONObject jsonObject, String path)
    {
        Object value = getJson(jsonObject, path);
        return value == null ? null : (Boolean) value;
    }

    /**
     * 获取path对应的Object值
     * @param jsonObject 操作的JSONObject
     * @return path对应的 Object 值，如果属性不存在，返回null
     */
    public static Object getJson(JSONObject jsonObject, String path)
    {

        return isEmpty(path) ? jsonObject : JSONPath.eval(jsonObject, path);
    }

    /**
     * 将 value 放入到指定路径的对象中，路径可以包含下标，缺少的路径或数据元素会自动补全
     * @param json  操作的json对象
     * @param path 路径
     * @param value 值
     */
    public static void putByPath(JSONObject json, String path, Object value)
    {

        JSONPath.set(json, path, value);
    }
}
