package jar.shaw.tools.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * properties文件读取工具类，实例化该类，即可读取properties文件各种类型的属性
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
public class PropertiesHelper
{

    private Properties properties = new Properties();

    /**
     * 创建PropertiesHelper
     * @param path 文件路径
     * @throws IOException IOException
     */
    public PropertiesHelper(String path) throws IOException, URISyntaxException {
        try
        {
            properties.load(Files.newInputStream(FileHelper.getPath(path)));
        }
        catch (IOException e)
        {
            throw new IOException("failed to read config " + FileHelper.getPath(path).toUri().getPath());
        }
    }

    /**
     * 创建PropertiesHelper
     * @param file 基于磁盘路径的属性文件
     * @throws IOException IOException
     */
    public PropertiesHelper(File file) throws IOException
    {
        try
        {
            properties.load(new FileInputStream(file));
        }
        catch (IOException e)
        {
            throw new IOException("failed to read config " + file.getAbsolutePath());
        }
    }

    /**
     * @return 该属性文件对应的Properties对象
     */
    public Properties getProperty()
    {
        return properties;
    }

    /**
     * 获取int属性
     * @param property 属性名
     * @param defaultValue 如果属性不存在时的默认值
     * @return 属性的int值，或者默认值
     */
    public int getInt(String property, int defaultValue) {
        String value = properties.getProperty(property);
        return value == null ? defaultValue : Integer.valueOf(value.trim());
    }

    /**
     * 获取int属性
     * @param property 属性名
     * @param defaultValue 如果属性不存在时的默认值
     * @return 属性的double值，或者默认值
     */
    public double getDouble(String property, double defaultValue) {
        String value = properties.getProperty(property);
        return value == null ? defaultValue : Double.valueOf(value.trim());
    }

    /**
     * 获取int属性
     * @param property 属性名
     * @param defaultValue 如果属性不存在时的默认值
     * @return 属性的float值，或者默认值
     */
    public float getFloat(String property, float defaultValue) {
        String value = properties.getProperty(property);
        return value == null ? defaultValue : Float.valueOf(value.trim());
    }

    /**
     * 获取int属性
     * @param property 属性名
     * @return 属性的String值，或者属性不存在时返回空字符串
     */
    public String getString(String property) {
        String value = properties.getProperty(property);
        return value == null ? "" : value.trim();
    }


}
