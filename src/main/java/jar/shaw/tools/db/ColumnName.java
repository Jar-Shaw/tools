package jar.shaw.tools.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 例如：{@code ColumnName("c1")}
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnName
{
    public String value();
}
