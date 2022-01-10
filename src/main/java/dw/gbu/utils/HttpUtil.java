package dw.gbu.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void main(String[] args) {
        String url = "http://192.168.15.219:8080/urule_gbu_war/urule/rest/gbu-demo/UruleCreditCount002";
        String param = "[\n" +
        "    {\n" +
        "        \"name\": \"参数\",\n" +
        "        \"fields\": {\n" +
        "            \"comp_code\": \"KHZC211213000001\"\n" +
        "        }\n" +
        "    }\n" +
        "]";
        String returnStr = new HttpUtil().sendPostRequest(url,param);
        System.out.println(returnStr);
    }


    /**
 * 模拟http协议发送post请求
 **/
public static String sendPostRequest(String url, String param) {
    InputStream in = null;
    String result = "";
    HttpURLConnection connection = null;

    try {
        URL httpUrl = new URL(url);
        connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.getOutputStream().write(param.getBytes("utf-8"));
        connection.getOutputStream().flush();

        if (connection.getResponseCode() == 200) {
            in = connection.getInputStream();

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            int n = 0;
            byte[] datas = new byte[2048];

            while ((n = in.read(datas)) != -1) {
                bs.write(datas, 0, n);
            }

            bs.flush();
            result = new String(bs.toByteArray(), "utf-8");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            connection.disconnect();
        } catch (Exception ex) {
        }
    }

    return result;
}
}
