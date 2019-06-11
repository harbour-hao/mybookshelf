package student.jndx.com.bookshelf;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;

/**
 * Created by starry_mei on 2019/4/25.
 */

public class Singleadd {
    public static String calcAuthorization(String source, String secretId, String secretKey, String datetime)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
        Mac mac = Mac.getInstance("HmacSHA1");
        Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(sKey);
        byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
        String sig = new BASE64Encoder().encode(hash);

        String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\"" + sig + "\"";
        return auth;
    }

    public static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                    URLEncoder.encode(entry.getValue().toString(), "UTF-8")
            ));
        }
        return sb.toString();
    }

    public static String getResult(String isbn) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        //云市场分配的密钥Id
        String secretId = "AKID3vBk0rXtp2xfouckMQ6vx7Dfed72jy16phle";
        //云市场分配的密钥Key
        String secretKey = "hzolp2kjp16bHu11ha6234tlvg53bn716gdQqvne";
        String source = "market";

        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String datetime = sdf.format(cd.getTime());
        // 签名
        String auth = calcAuthorization(source, secretId, secretKey, datetime);
        // 请求方法
        String method = "GET";
        // 请求头
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Source", source);
        headers.put("X-Date", datetime);
        headers.put("Authorization", auth);

        // 查询参数
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("isbn", isbn);
        // body参数
        Map<String, String> bodyParams = new HashMap<String, String>();

        // url参数拼接
        String url = "https://service-osj3eufj-1255468759.ap-shanghai.apigateway.myqcloud.com/release/isbn";
        if (!queryParams.isEmpty()) {
            url += "?" + urlencode(queryParams);
        }

        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(method);

            // request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // request body
            Map<String, Boolean> methods = new HashMap<>();
            methods.put("POST", true);
            methods.put("PUT", true);
            methods.put("PATCH", true);
            Boolean hasBody = methods.get(method);
            if (hasBody != null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(urlencode(bodyParams));
                out.flush();
                out.close();
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            Log.d("json",result);
            return result;

        } catch (Exception e) {
            Log.d("hh", "fault ");
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return "no get";
    }
    public static Book parseJson(Context context, String text, String isbn) {
        Book new_book=new Book();
        new_book.setISBN(isbn);
        String url="";
        try {

            Log.d("try successfully", " in");
            Log.d("try successfully", text);
            //这里的text就是上边获取到的数据，一个String.
            JSONObject jsonObject = new JSONObject(text);
            Log.d("try successfully", " 1");
            //通过查看豆瓣图书的json数据 可得知其书名是保存在tags数组里面的。
            JSONObject jsonDatas = jsonObject.getJSONObject("showapi_res_body").getJSONObject("data");
            //JSONObject bookJson = jsonDatas.getJSONObject(0);
            new_book.setTitle(jsonDatas.getString("title"));
            new_book.setWriter(jsonDatas.getString("author"));
            new_book.setPublisher(jsonDatas.getString("publisher"));
            new_book.setDate(jsonDatas.getString("pubdate"));
            new_book.setISBN(jsonDatas.getString("isbn"));
            url=jsonDatas.getString("img");
            Bitmap bit= Parsebook.ImageManager.decodeSampledBitmapFromUrl(context, url);
            Parsebook.ImageManager.SaveImage(context,bit,new_book.getUuid());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new_book;
    }


}
