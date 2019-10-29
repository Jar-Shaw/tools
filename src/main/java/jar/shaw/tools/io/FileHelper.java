package jar.shaw.tools.io;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static jar.shaw.tools.util.ExceptionHelper.runtime;
import static java.util.Collections.reverseOrder;

/**
 * 常用的文件操作
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 </p>
 */
public class FileHelper
{
    /**
     * 用UTF-8编码将classpath上指定文件整个读成一个字符串返回，支持读取jar包中的文件
     * @param path 基于classpath的文件路径，必须以/开头
     * @return 文件内容
     * @throws URISyntaxException URISyntaxException
     * @throws IOException IOException
     */
    public static String readFileString(String path)
    {
        return new String(readFileBytes(path), StandardCharsets.UTF_8);
    }

    /**
     * 用指定的编码将classpath上指定文件整个读成一个字符串返回，支持读取jar包中的文件
     * @param path 基于classpath的文件路径，必须以/开头
     * @param charset 编码方式
     * @return 文件内容
     * @throws URISyntaxException URISyntaxException
     * @throws IOException IOException
     */
    public static String readFileString(String path, Charset charset)
    {
        return new String(readFileBytes(path), charset);
    }

    /**
     * 读取classpath上指定文件的所有字节，包括jar包里的文件
     * @param path 基于classpath的文件路径，必须以/开头
     * @return 指定文件的字节码
     * @throws IOException IOException
     */
    public static byte[] readFileBytes(String path)
    {
        InputStream is = FileHelper.class.getResourceAsStream(path);
        byte[] bytes = readFileBytes(is);
        runtime(is::close);
        return bytes;
    }

    /**
     * 读取classpath上指定文件的所有字节
     * @param is 输入流
     * @return 指定文件的字节码
     * @throws IOException IOException
     */
    public static byte[] readFileBytes(InputStream is)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4 * 1024];
        runtime(() -> {
            int i = -1;
            while ((i = is.read(buf)) > 0) {
                bos.write(buf, 0, i);
            }
            bos.flush();
        });
        byte[] bytes = bos.toByteArray();
        runtime(bos::close);
        return bytes;
    }

    /**
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 得到classpath上指定路径的{@link java.nio.file.Path}，支持jar包中的文件路径，
     * 但不支持jar包里嵌套的jar内部的路径，因为ZipFileSystem仅支持zip文件是文件系统能直接读取的文件。
     * <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 该方法主要用于得到基于classpath的文件路径的Path对象，如果需要读取classpath里的文件，
     * 建议使用工具类里的其他方法。
     * @param path 基于classpath的文件路径，/表示classpath根路径
     * @return 指定路径对应的Path对象
     */
    public static Path getPath(String path)
    {
        AtomicReference<Path> ref = new AtomicReference<>();
        runtime(() -> {
            Path p;
            URL url = FileHelper.class.getResource(path);
            if (url == null) {
                throw new FileNotFoundException(path + " not found");
            }
            URI uri = url.toURI();
            if ("jar".equals(uri.getScheme())) {
                String uriString = uri.toString();
                int i = uriString.indexOf("!");
                FileSystem fs;
                try {
                    fs = FileSystems.newFileSystem(URI.create(uriString.substring(0, i)), new HashMap<>());
                } catch (FileSystemAlreadyExistsException e) {
                    //This can read file from the same jar more than one times
                    fs = FileSystems.getFileSystem(URI.create(uriString.substring(0, i)));
                }
                //address spring boot jar, spring classes under /BOOT-INF/classes, e.g.:
                //jar:file:/D:/git/emr2DetailService/target/emr2DetailService-1.0-SNAPSHOT.jar!/BOOT-INF/classes!/PatientDetailTransform3_0_7.json
                p = fs.getPath(uriString.substring(i + 1).replace("!",""));
            } else {
                p = Paths.get(uri);
            }
            ref.set(p);
        });
        return ref.get();
    }


    /**
     * 删除指定的文件，可以是文件夹
     * @param path 基于磁盘目录的路径，可以是磁盘绝对路径或相对路径
     * @throws IOException IOException
     */
    public static void deleteFile(String path)
    {
        runtime(() -> Files.walk(Paths.get(path))
            .sorted(reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete));
    }
}
