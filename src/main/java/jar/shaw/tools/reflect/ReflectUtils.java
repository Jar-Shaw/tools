package jar.shaw.tools.reflect;

import java.io.File;
import java.lang.reflect.Method;
import java.net.*;

import static jar.shaw.tools.util.ExceptionHelper.runtime;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2018/12/21</p>
 */
public class ReflectUtils {

    /**
     * 获取类的磁盘路径
     * @param clazz 类对象
     * @return 类的磁盘路径
     */
    public static String locateClass(Class clazz) {
        String clsAsResource = "/" + clazz.getName().replace('.', '/').concat(".class");
        return clazz.getResource(clsAsResource).toString();
    }

    /**
     * 运行时添加classpath
     * @param url 目录或者Jar文件的URL
     */
    public static void addClasspath(URL url) {
        runtime(() -> {
            URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        });
    }

    /**
     * 获取class文件所在的classpath的路径，或者包含calss的jar包（支持springboot jar）
     * @param clazz
     * @return
     */
    public static File getClasspathDirOrJar(Class clazz) {
        String clsAsResource = "/" + clazz.getName().replace('.', '/').concat(".class");
        String resourceURL = clazz.getResource(clsAsResource).toString();

        // if class load from jar
        // jar:file:/D:/.m2/repository/junit/junit/4.11/junit-4.11.jar!/org/junit/Test.class
        if (resourceURL.startsWith("jar")) {
            resourceURL = resourceURL.substring(4);
            // if it's springboot jar
            // jar:file:/D:/git/gennlife-tools/related-word-loader/target/related-word-loader-1.0.0.jar!/BOOT-INF/classes!/
            if (resourceURL.contains(".jar!/BOOT-INF/classes!/")) {
                resourceURL = resourceURL.substring(0, resourceURL.indexOf("/BOOT-INF/classes!" + clsAsResource) - 1);
            } else {
                resourceURL = resourceURL.substring(0, resourceURL.indexOf(clsAsResource) - 1);
            }
        } else {
            // file:/D:/git/convenience/target/test-classes/com/gennlife/convnce/db/AppTest.class
            resourceURL = resourceURL.substring(0, resourceURL.indexOf(clsAsResource));
        }
        String filePath = null;
        try {
            filePath = new URI(resourceURL).getPath();
        } catch (URISyntaxException e) {
            runtime(e);
        }
        return new File(filePath);
    }
}
