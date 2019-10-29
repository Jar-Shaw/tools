package jar.shaw.tools.lambda;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2019/8/15</p>
 */
public interface ThrowableSupplier<T>
{

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws Exception;

}
