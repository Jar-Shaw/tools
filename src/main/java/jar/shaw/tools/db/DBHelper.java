package jar.shaw.tools.db;

import javafx.util.Pair;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static jar.shaw.tools.util.ExceptionHelper.runtime;
import static jar.shaw.tools.db.Model.getColumnAndGetters;
import static jar.shaw.tools.db.Model.getColumnAndTypes;
import static jar.shaw.tools.db.Model.getTableName;

/**
 * 数据库操作类
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
public class DBHelper
{

    /**
     * 批量插入实体对象，该方法不会调用connection.commit()<p />
     * 调用此方法前将autocommit设置为false以提高效率，调用完需要手动调用commit<p />
     * 实体到表的映射规则有以下两种，没有注解时采用方法1：<p />
     * 1、类名按驼峰分割变为小写后，用下划线拼接作为表名，字段名与实体字段名一致<p />
     * 2、由{@link TableName}和{@link ColumnName}指定
     * @param connection 数据库连接
     * @param objects 要保存的实体对象数组
     * @throws SQLException SQLException
     */
    public static void batchInsert(Connection connection, List<?> objects) throws SQLException
    {

        if (objects.size() == 0)
        {
            return;
        }
        Class<?> clazz = objects.get(0).getClass();
        PreparedStatement prepstmt = connection.prepareStatement(buildInsertSQL(clazz));
        List<Pair<String, Method>> model = getColumnAndGetters(clazz);

        //设置参数值
        for (Object o : objects)
        {
            for (int i = 0; i < model.size(); i++)
            {
                int k = i;
                runtime(() -> prepstmt.setObject(k + 1, model.get(k).getValue().invoke(o)));
            }
            prepstmt.addBatch();
        }
        prepstmt.executeBatch();
        prepstmt.close();
    }

    /**
     * 插入单个实体对象，该方法不会调用connection.commit()<p />
     * 实体到表的映射规则有以下两种，没有注解时采用方法1：<p />
     * 1、类名按驼峰分割变为小写后，用下划线拼接作为表名，字段名与实体字段名一致<p />
     * 2、由{@link TableName}和{@link ColumnName}指定
     * @param connection 数据库连接
     * @param object 要保存的实体对象
     * @throws SQLException SQLException
     */
    public static void insert(Connection connection, Object object) throws SQLException
    {
        Class<?> clazz = object.getClass();
        PreparedStatement prepstmt = connection.prepareStatement(buildInsertSQL(clazz));
        List<Pair<String, Method>> model = getColumnAndGetters(clazz);
        for (int i = 0; i < model.size(); i++)
        {
            int k = i;
            runtime(() -> prepstmt.setObject(k + 1, model.get(k).getValue().invoke(object)));
        }
        prepstmt.execute();
        prepstmt.close();
    }

    /**
     * 根据类构建PrepareStatement对应的sql语句
     * @param clazz 实体类名
     * @return PrepareStatement形式的sql语句
     */
    private static String buildInsertSQL(Class<?> clazz)
    {
        //获得表名和字段信息
        String tableName = getTableName(clazz);
        List<Pair<String, Method>> model = getColumnAndGetters(clazz);

        //构建sql语句
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(tableName).append("(");
        StringBuilder paras = new StringBuilder();
        model.forEach(p -> {
            sb.append(p.getKey()).append(",");
            paras.append("?,");
        });
        sb.setCharAt(sb.length() - 1, ')');
        sb.append(" VALUES(").append(paras);
        sb.setCharAt(sb.length() - 1, ')');
        return sb.toString();
    }

    /**
     * <p>根据实体类创建表</p>
     * <p>1、类名按驼峰分割变为小写后，用下划线拼接作为表名，字段名与实体字段名一致</p>
     * <p>2、由{@link TableName}和{@link ColumnName}指定</p>
     * @param connection 数据库连接
     * @param clazz 实体类
     * @throws SQLException SQLException
     */
    public static void createTable(Connection connection, Class<?> clazz) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.execute(buildTableSQL(clazz));
        statement.close();
    }

    /**
     * <p>根据实体类得到创建表的SQL</p>
     * <p>1、类名按驼峰分割变为小写后，用下划线拼接作为表名，字段名与实体字段名一致</p>
     * <p>2、由{@link TableName}和{@link ColumnName}指定</p>
     * @param clazz 实体类
     * @throws SQLException SQLException
     */
    public static String buildTableSQL(Class<?> clazz) throws SQLException
    {
        String tableName = getTableName(clazz);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `").append(tableName).append("`(\n");
        sb.append("id  bigint(20) NOT NULL AUTO_INCREMENT ,\n");
        getColumnAndTypes(clazz).forEach(p -> sb.append("`").append(p.getKey()).append("` ").append(p.getValue()).append(" ,\n"));
        sb.append("PRIMARY KEY (`id`)\n");
        sb.append(")");
        return sb.toString();
    }

    /**
     * 执行包含多个SQL语句的SQL脚本
     * @param connection 数据库连接
     * @param script 脚本字符串
     * @throws SQLException SQLException
     */
    public static void executeSQLScript(Connection connection, String script) throws SQLException {
        Statement statement = connection.createStatement();
        String[] sqls = script.split(";");
        for (String sql : sqls) {
            sql = sql.trim();
            if (sql.length() > 0) {
                statement.execute(sql);
            }
        }
        statement.close();
    }

}
