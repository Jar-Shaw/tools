package jar.shaw.tools.util;

import java.util.Stack;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2018/12/20</p>
 */
public class Timing {

    private Stack<Long> time = new Stack<>();

    public void start() {
        time.push(System.currentTimeMillis());
    }

    public long stop() {
        return System.currentTimeMillis() - time.pop();
    }
}
