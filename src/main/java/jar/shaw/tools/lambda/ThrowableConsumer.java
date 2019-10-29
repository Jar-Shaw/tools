package jar.shaw.tools.lambda;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2018/12/6</p>
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws Exception;
}
