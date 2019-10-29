package jar.shaw.tools.io;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static jar.shaw.tools.util.ExceptionHelper.runtime;

/**
 * @author 肖佳
 * @since 1.8
 * <p>2019/7/19</p>
 */
public class HttpHelper {

    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * 用post方法发送http请求，参数是json字符串，返回的数据也是字符串
     * @param url 请求的url
     * @param para json字符串
     * @return 响应字符串
     */
    public static String post(String url, String para) {

        try
        {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new ByteArrayEntity(para.getBytes(StandardCharsets.UTF_8)));
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            HttpResponse response = httpClient.execute(httpPost);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200)
            {
                byte[] body = EntityUtils.toByteArray(response.getEntity());
                return new String(body);
            } else
            {
                throw new RuntimeException("HTTP ERROR: " + responseCode + ", " + response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e)
        {
            runtime(e);
        }
        return null;
    }
}
