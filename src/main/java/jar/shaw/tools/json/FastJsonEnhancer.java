package jar.shaw.tools.json;

import com.alibaba.fastjson.*;

import java.util.regex.Pattern;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2019/5/22</p>
 */
public class FastJsonEnhancer extends JSONObject {

    private static final Pattern arrayPattern = Pattern.compile("(.*)\\[([0-9]+)]$");

    private FastJsonEnhancer(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static FastJsonEnhancer enhance(JSONObject jsonObject) {
        return new FastJsonEnhancer(jsonObject);
    }

    /**
     * 获取path对应的 JSONArray 值
     * @param path 能唯一确定一个值的path
     * @return path对应的 JSONArray 值，如果属性不存在，返回null
     */
    public JSONArray getJSONArrayByPath(String path)
    {
        return FastJsonHelp.getJSONArray(this, path);
    }

    /**
     * 获取path对应的 JSONObject 值
     * @param path 能唯一确定一个值的path
     * @return path对应的 JSONObject 值，如果属性不存在，返回null
     */
    public JSONObject getJSONObjectByPath(String path)
    {
        return FastJsonHelp.getJSONObject(this, path);
    }

    /**
     * 目前仅支持设置某个属性的值
     * @param path 路径
     * @param value 值
     */
    @Deprecated
    public void setByPath(String path, Object value) {
        if (path.contains(".")) {
            getJSONObjectByPath(path.substring(0, path.lastIndexOf("."))).put(path.substring(path.lastIndexOf(".") + 1), value);
        } else {
            this.put(path, value);
        }
    }

    /**
     * 将 value 放入到指定路径的对象中，路径可以包含下标，缺少的路径或数据元素会自动补全
     * @param path 路径
     * @param value 值
     */
    public void putByPath(String path, Object value)
    {
        FastJsonHelp.putByPath(this, path, value);
    }


    /**
     * 获取path对应的 String 值
     * @param path 能唯一确定一个值的path
     * @return 获取path对应的 String 值，如果属性不存在，返回null
     */
    public String getStringByPath(String path)
    {
        return FastJsonHelp.getJsonString(this, path);
    }

    /**
     * 获取path对应的 Integer 值
     * @param path 能唯一确定一个值的path
     * @return path对应的 Integer 值，如果属性不存在，返回null
     */
    public Integer getIntByPath(String path)
    {
        return FastJsonHelp.getJsonInt(this, path);
    }

    /**
     * 获取path对应的 Double 值
     * @param path 能唯一确定一个值的path
     * @return path对应的 Double 值，如果属性不存在，返回null
     */
    public Double getDoubleByPath(String path)
    {
        return FastJsonHelp.getJsonDouble(this, path);

    }

    /**
     * 获取path对应的 Object
     * @param path 能唯一确定一个值的path
     * @return path对应的 Object ，如果属性不存在，返回null
     */
    public Object getByPath(String path)
    {
        return FastJsonHelp.getJson(this, path);
    }

}
