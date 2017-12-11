package lifeng.gankqzone.network;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import lifeng.gankqzone.util.LoggerUtils;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by 峰 on 14:09
 * 自定义的网络请求日志拦截
 */
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String requestMessage = "";

        //the request url
        String url = request.url().toString();
        //the request method
        String method = request.method();

        long t1 = System.nanoTime();//返回的是纳秒

        Log.v("TTT", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>网络请求日志___start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        requestMessage = "Request method:" + method + "\n";
        //the request body
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            StringBuilder sb = new StringBuilder();
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset));
                sb.append(" (Content-Type = ").append(contentType.toString()).append(",")
                        .append(requestBody.contentLength()).append("-byte body)");
            } else {
                sb.append(" (Content-Type = ").append(contentType.toString())
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
//            sb.append("]");
            requestMessage = requestMessage + "Request body:  " + String.format(Locale.getDefault(), "%s", sb.toString()) + " \n";
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        //the response time
        requestMessage = requestMessage + "Response code:" + response.code() + "\n";
        requestMessage = requestMessage + "Response message:" + response.message() + "\n";
        requestMessage = requestMessage + "URL:" + url + "\n";
        requestMessage = requestMessage + "Response time:" + ((t2 - t1) / 1e6d)/1000 + " s\n";

        ResponseBody body = response.body();

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.defaultCharset();
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
            String bodyString = buffer.clone().readString(charset);
            requestMessage = requestMessage + "Response body:\n" + bodyString;
        }

        LoggerUtils.w(requestMessage);

        Log.v("TTT", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<网络请求日志___end<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return response;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}