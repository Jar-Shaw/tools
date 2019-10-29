package jar.shaw.tools.util;

import jar.shaw.tools.lambda.ThrowableRunnable;
import jar.shaw.tools.lambda.ThrowableSupplier;
import org.slf4j.Logger;

/**
 * 异常帮助类，可以将捕获异常转换为运行时异常，并记录日志
 *
 * @author 肖佳
 * @since 1.8
 * <p>创建时间：2018/11/6 15:28</p>
 */
public class ExceptionHelper {


    /**
     * 忽略该捕获异常
     * @param runnable 抛出异常的函数表达式
     */
    public static void ignore(ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception ignore) {}
    }

    /**
     * 忽略该捕获异常，并记录日志
     * @param log 日志对象
     * @param runnable 抛出异常的函数表达式
     */
    public static void ignoreWithLog(Logger log, ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 绕过编译器检查将捕获异常抛出，从而不需要捕获该异常
     * @param runnable 抛出异常的函数表达式
     * @param <X> X
     * @throws X X
     */
    @SuppressWarnings("unchecked")
    public static <X extends Throwable> void runtime(ThrowableRunnable runnable) throws X {
        try {
            runnable.run();
        } catch (Exception e) {
            //this is an amazing behavior, convert the Exception to a general type and throw it,
            //then the caller do not need to catch it
            throw (X)e;
        }
    }

    /**
     * 绕过编译器检查将捕获异常抛出，从而不需要捕获该异常
     * @param supplier 可抛异常的函数表达式
     * @param <X> X
     * @throws X X
     */
    @SuppressWarnings("unchecked")
    public static <T, X extends Throwable> T runtime(ThrowableSupplier<T> supplier) throws X {
        try {
            return supplier.get();
        } catch (Exception e) {
            //this is an amazing behavior, convert the Exception to a general type and throw it,
            //then the caller do not need to catch it
            throw (X)e;
        }
    }

    /**
     * 绕过编译器检查将捕获异常抛出，从而不需要捕获该异常，该方法主要用于catch部分
     * @param e 异常
     * @param <X> X
     * @throws X X
     */
    @SuppressWarnings("unchecked")
    public static <X extends Throwable> void runtime(Exception e) throws X {
        //this is an amazing behavior, convert the Exception to a general type and throw it,
        //then the caller do not need to catch it
        throw (X)e;
    }

    /**
     * 将捕获异常转换成运行时异常抛出，并记录日志
     * @param log 日志对象
     * @param runnable 抛出异常的函数表达式
     * @param <X> X
     * @throws X X
     */
    @SuppressWarnings("unchecked")
    public static <X extends Throwable> void runtimeWithLog(Logger log, ThrowableRunnable runnable) throws X {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw (X)e;
        }
    }

    /**
     * 将捕获异常转换成运行时异常抛出，并记录日志，该方法主要用于try-with-resource的catch部分使用
     * @param log 日志对象
     * @param e 异常
     * @param <X> X
     * @throws X X
     */
    @SuppressWarnings("unchecked")
    public static <X extends Throwable> void runtimeWithLog(Logger log, Exception e) throws X {
        log.error(e.getLocalizedMessage(), e);
        throw (X)e;
    }
}
