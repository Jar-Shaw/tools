package jar.shaw.tools.lambda;

/**
 * 可抛异常的Runnable
 *
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Exception;
}
