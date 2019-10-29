package jar.shaw.tools.db;

import jar.shaw.tools.util.MapBuilder;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static jar.shaw.tools.util.Utils.getAnnotation;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * 用于处理类与数据库表的映射关系
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6</p>
 */
public class Model
{

    private static Map<Class<?>, String> java2sqlTypeMaping = new MapBuilder()
        .put(String.class, "varchar(255)")
        .put(Long.class, "long")
        .put(Integer.class, "int")
        .put(Short.class, "smallint")
        .put(Byte.class, "tinyint")
        .put(Boolean.class, "bit")
        .put(Double.class, "double")
        .put(Float.class, "float")
        .put(java.sql.Date.class, "date")
        .put(java.util.Date.class, "datetime")
        .build();

    /**
     * <p>获取类对应的表名</p>
     * <p>1、类名按驼峰分割变为小写后，用下划线拼接作为表名，字段名与实体字段名一致</p>
     * <p>2、由{@link TableName}和{@link ColumnName}指定</p>
     * @param clazz 类名
     * @return 类名对应的表名
     */
    public static String getTableName(Class<?> clazz)
    {

        TableName tableAnnotation = getAnnotation(clazz, TableName.class);
        if (tableAnnotation != null)
        {
            return tableAnnotation.value();
        }

        List<String> tokens = new ArrayList<>();
        String className =  clazz.getSimpleName();
        StringBuilder sb = new StringBuilder();
        int len = className.length();
        for (int i = 0; i < len; i++)
        {
            int c = className.charAt(i);
            if (isNotCapital(c))
            {
                sb.append((char)c);
            }
            else
            {
                if (sb.length() > 0)
                {
                    //the last token ended
                    tokens.add(sb.toString());
                    sb.delete(0, sb.length());
                }
                sb.append((char)(c + 'a' - 'A'));
                //find the next non Capital letter
                int nextNonCapitalIndex = -1;
                for (int j = i + 1; j < className.length(); j++)
                {
                    if (isNotCapital(className.charAt(j)))
                    {
                        nextNonCapitalIndex = j;
                        break;
                    }
                }

                //if this capital is the begin of a abbreviation
                //nextNonCapitalIndex == -1 indicate capital extends to the end
                if (nextNonCapitalIndex - i > 1 || nextNonCapitalIndex == -1)
                {
                    //k is the abbreviation end index
                    int k = nextNonCapitalIndex == -1 ?  len - 1 : nextNonCapitalIndex - 2;
                    for (int j = i + 1; j <= k; j++)
                    {
                        sb.append((char)(className.charAt(j) + 'a' - 'A'));
                    }
                    tokens.add(sb.toString());
                    sb.delete(0, sb.length());
                    i = k;
                }
            }
        }
        if (sb.length() > 0)
        {
            tokens.add(sb.toString());
        }
        return tokens.stream().collect(joining("_"));

    }

    private static boolean isNotCapital(int c)
    {
        return c < 'A' || c > 'Z';
    }

    static List<Pair<String, Method>> getColumnAndGetters(Class<?> clazz)
    {
        Map<String, Method> methodName2method = Stream.of(clazz.getMethods())
            .filter(m -> m.getName().toLowerCase().startsWith("get"))
            .map(m -> new Pair<>(m.getName().toLowerCase(), m))
            .collect(toMap(Pair::getKey, Pair::getValue));
        List<Pair<String, Method>> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields())
        {
            String fieldName = field.getName();
            Method getter = methodName2method.get("get" + fieldName.toLowerCase());
            if (getAnnotation(field, ExclusiveColumn.class) == null)
            {
                ColumnName annotation = getAnnotation(field, ColumnName.class);
                result.add(annotation != null ? new Pair<>(annotation.value(), getter) : new Pair<>(fieldName, getter));
            }
        }
        return result;
    }

    static List<Pair<String, String>> getColumnAndTypes(Class<?> clazz)
    {
        List<Pair<String, String>> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields())
        {
            String type = java2sqlTypeMaping.get(field.getType());
            if (getAnnotation(field, ExclusiveColumn.class) == null)
            {
                ColumnName annotation = getAnnotation(field, ColumnName.class);
                result.add(annotation != null ? new Pair<>(annotation.value(), type) : new Pair<>(field.getName(), type));
            }
        }
        return result;
    }
}
