package jar.shaw.tools.json;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static jar.shaw.tools.util.TypeTransHelper.intOf;

/**
 * <p>Gson操作帮助类，可以快速获取指定path需要的值，得到Map、List等</p>
 *
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
public class GsonHelper {

    private static Gson gson = new Gson();

    /**
     * 将Gson的JsonObject转换为Map
     * @param jsonObject 操作的jsonObject
     * @return JsonObject对应的Map
     */
    @SuppressWarnings({"unchecked", "JavaReflectionMemberAccess"})
    public static Map<String, JsonElement> gsonToMap(JsonObject jsonObject)
    {
        try
        {
            Field field = JsonObject.class.getField("members");
            field.setAccessible(true);
            return (Map<String, JsonElement>) field.get(jsonObject);
        }
        catch (ClassCastException | NoSuchFieldException | IllegalAccessException e)
        {
            return jsonObject.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    /**
     * 将Gson的JsonArray转换为List
     * @param jsonArray 操作的jsonObject
     * @return JsonArry对应的List
     */
    @SuppressWarnings({"unchecked", "JavaReflectionMemberAccess"})
    public static List<JsonElement> gsonToList(JsonArray jsonArray)
    {
        try
        {
            Field field = JsonObject.class.getField("elements");
            field.setAccessible(true);
            return (List<JsonElement>) field.get(jsonArray);
        }
        catch (ClassCastException | NoSuchFieldException | IllegalAccessException e)
        {
            List<JsonElement> jsonElements = new ArrayList<>();
            jsonArray.forEach(jsonElements::add);
            return jsonElements;
        }
    }

    /**
     * 获取path对应的 JsonArray 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 JsonArray 值，如果属性不存在，返回null
     */
    public static JsonArray getJsonArray(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? new JsonArray() : jsonElement.getAsJsonArray();
    }

    /**
     * 获取path对应的 JsonObject 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 JsonObject 值，如果属性不存在，返回null
     */
    public static JsonObject getJsonObject(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? new JsonObject() : jsonElement.getAsJsonObject();
    }

    /**
     * 获取path对应的 String 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return 获取path对应的 String 值，如果属性不存在，返回null
     */
    public static String getJsonString(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? null : jsonElement.getAsString();
    }

    /**
     * 获取path对应的 Integer 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 Integer 值，如果属性不存在，返回null
     */
    public static Integer getJsonInt(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? null : jsonElement.getAsInt();
    }

    /**
     * 获取path对应的 Float 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 Float 值，如果属性不存在，返回null
     */
    public static Float getJsonFloat(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? null : jsonElement.getAsFloat();

    }

    /**
     * 获取path对应的 Double 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 Double 值，如果属性不存在，返回null
     */
    public static Double getJsonDouble(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? null : jsonElement.getAsDouble();

    }

    /**
     * 获取path对应的 Boolean 值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return 获取path对应的 Boolean 值，如果属性不存在，返回null
     */
    public static Boolean getJsonBoolean(JsonObject jsonObject, String path)
    {
        JsonElement jsonElement = getJson(jsonObject, path);
        return jsonElement == null || jsonElement.isJsonNull() ? null : jsonElement.getAsBoolean();
    }

    /**
     * 获取path对应的JsonElement值
     * @param jsonObject 操作的JsonObject
     * @param path 能唯一确定一个值的path
     * @return path对应的 JsonElement 值，如果属性不存在，返回null
     */
    private static JsonElement getJson(JsonObject jsonObject, String path)
    {

        if (path == null || path.trim().length() == 0)
        {
            return jsonObject;
        }

        Pattern pattern = Pattern.compile("(.*)\\[([0-9]+)]$");
        JsonElement jsonElement = jsonObject;
        for(String token : path.trim().split("\\."))
        {
            if (jsonElement == null)
            {
                return null;
            }
            JsonObject parent = jsonElement.getAsJsonObject();
            Matcher matcher = pattern.matcher(token);
            if(matcher.find())
            {
                String key = matcher.group(1);
                int index = intOf(matcher.group(2));
                jsonElement = parent.getAsJsonArray(key).get(index);
            } else {
                jsonElement = parent.get(token);
            }
        }
        return jsonElement;
    }

}
