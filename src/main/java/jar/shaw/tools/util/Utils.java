package jar.shaw.tools.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static jar.shaw.tools.util.ExceptionHelper.runtime;

/**
 * 各种各样的方法
 *
 * @author 肖佳
 * @since 1.8
 * <p>2018/11/6</p>
 */
public class Utils {
    /**
     * 获取字段指定类型的注解
     *
     * @param field 字段
     * @param clazz 注解类型
     * @param <T>   注解类型
     * @return 如果该注解存在则返回该注解，否则返回null
     */
    public static <T extends Annotation> T getAnnotation(Field field, Class<T> clazz) {
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation.annotationType() == clazz) {
                return clazz.cast(annotation);
            }
        }
        return null;
    }

    /**
     * 获取方法指定类型的注解
     *
     * @param method 方法
     * @param clazz  注解类型
     * @param <T>    注解类型
     * @return 如果该注解存在则返回该注解，否则返回null
     */
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation.annotationType() == clazz) {
                return clazz.cast(annotation);
            }
        }
        return null;
    }

    /**
     * 获取类指定类型的注解
     *
     * @param clazz1 类
     * @param clazz  注解类型
     * @param <T>    注解类型
     * @return 如果该注解存在则返回该注解，否则返回null
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz1, Class<T> clazz) {
        for (Annotation annotation : clazz1.getDeclaredAnnotations()) {
            if (annotation.annotationType() == clazz) {
                return clazz.cast(annotation);
            }
        }
        return null;
    }

    /**
     * <p>当对象不为空时进行操作并返回值</p>
     * 该方法主要提高代码简洁性，例如：
     * <pre>
     *     String value = null;
     *     String s = imageInfo.getImageURL();
     *     if(!isEmpty(s)){
     *         value = jsonObject.getAsJsonString(s);
     *     }
     * </pre>
     * 如果采用 getIfNotEmpty，可以很简洁的写成
     * <pre>
     *     String value = getIfNotEmpty(imageInfo.getImageURL(), s -> jsonObject::getAsJsonString);
     * </pre>
     *
     * @param o        判断是否为空的对象
     * @param function 以o作为参数的function
     * @param <T>      操作对象的类型
     * @param <R>      返回结果的类型
     * @return 如果o对象不为null，并且o是字符串时不为""，则返回function的执行结果，否则返回null
     * @see #isEmpty(Object)
     */
    public static <T, R> R getIfNotEmpty(T o, Function<T, R> function) {
        if (!isEmpty(o)) {
            return function.apply(o);
        }
        return null;
    }


    /**
     * <p>当对象不为空时进行操作</p>
     * 主要用于提高代码简洁性，例如：
     * <pre>
     *     String s = imageInfo.getImageURL();
     *     if(!isEmpty(s)){
     *         jsonObject.addProperty(IMAGE_URL, s);
     *     }
     * </pre>
     * 如果采用 ifNotEmpty，可以很简洁的写成
     * <pre>
     *     ifNotEmpty(imageInfo.getImageURL(), s -> jsonObject.addProperty(IMAGE_URL, s));
     * </pre>
     *
     * @param o        判断是否为空的字符串
     * @param consumer 以s作为参数的consumer
     * @param <T>      操作对象的类型
     * @see #isEmpty(Object)
     */
    public static <T> void ifNotEmpty(T o, Consumer<T> consumer) {
        if (!isEmpty(o)) {
            consumer.accept(o);
        }
    }

    /**
     * <p>判断对象是否为空，对象为空包括如下几种情况：</p>
     * <ol>
     * <li>当对象为String时，对象为null或者为空字符串""
     * <li>当对象有size()方法时，对象为null或者size返回值为0</li>
     * <li>其他情况时，对象为null</li>
     * </ol>
     *
     * @param o 判断是否为空的对象
     * @return 如果对象属于上述情况之一，返回true，否则返回false
     */
    public static boolean isEmpty(Object o) {
        boolean isEmpty = true;
        Method sizeMethod = null;
        try {
            Class<?> clazz = o.getClass();
            sizeMethod = clazz.getMethod("size");
        } catch (Exception ignore) {
        }

        if (o instanceof String) {
            if (!"".equals(o)) {
                isEmpty = false;
            }
        } else if (sizeMethod != null) {
            boolean isZeroSize = true;
            try {
                if ((int) sizeMethod.invoke(o) > 0) {
                    isZeroSize = false;
                }
            } catch (Exception ignore) {
            }
            if (!isZeroSize) {
                isEmpty = false;
            }
        } else if (o != null) {
            isEmpty = false;
        }
        return isEmpty;
    }

    /**
     * <p>判断对象是否为空，对象为空包括如下几种情况：</p>
     * <ol>
     * <li>当对象为String时，对象为null或者为空字符串""
     * <li>当对象有size()方法时，对象为null或者size返回值为0</li>
     * <li>其他情况时，对象为null</li>
     * </ol>
     *
     * @param o 判断是否为空的对象
     * @return 如果对象属于上述情况之一，返回false，否则返回true
     */
    public static boolean notEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 转置二维矩阵
     * @param data 要转置的数据
     * @return 转置后的结果
     */
    public static String[][] transposition(List<List<Object>> data) {
        if(data == null || data.size() == 0) return null;
        String[][] result = new String[data.get(0).size()][data.size()];
        //not use index to traverse, when data's type is LinkedList, this can improve performance
        int i = 0, j = 0;
        for (List<Object> columns : data) {
            for (Object column : columns) {
                result[j][i] = column.toString();
                j++;
            }
            i++;
            j = 0;
        }

        return result;
    }

}
