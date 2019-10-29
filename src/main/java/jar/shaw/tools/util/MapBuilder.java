package jar.shaw.tools.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>用于构造带有初始值的hashmap</p>
 * <p>MapBuilder不对map的元素做类型检查，如果元素类型不能被转成目标类型，则会抛出转换异常</p>
 *
 * @author 肖佳
 * @since 1.7
 * <p>创建时间：2018/11/6 15:28</p>
 */
public class MapBuilder
{
    private Map<Object, Object> map;

    /**
     * <p>创建一个Map构造器用于初始化map</p>
     * <p>MapBuilder不对map的元素做类型检查，如果元素类型不能被转成目标类型，则会抛出转换异常</p>
     */
    public MapBuilder()
    {
        map = new HashMap<>();
    }

    /**
     * 添加key/value，如果添加的key/value类型不能被转成目标类型，则会抛出转换异常
     * @param key 添加的key
     * @param value 添加的value
     * @return MapBuilder本身
     */
    public MapBuilder put(Object key, Object value)
    {
        map.put(key, value);
        return this;
    }

    /**
     * 构建map
     * @param <K> 目标Map的key的类型
     * @param <V> 目标Map的value的类型
     * @return 构建的map
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K,  V> build()
    {
        Map<K, V> map1 = new HashMap<>();
        map.forEach((k, v) -> map1.put((K) k, (V) v));
        return map1;
    }
}
