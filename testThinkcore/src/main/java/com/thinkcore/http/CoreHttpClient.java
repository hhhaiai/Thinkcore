package com.thinkcore.http;

import com.thinkcore.utils.log.TLog;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * retrofit工具类
 */
public class CoreHttpClient {
    private static String Tag = CoreHttpClient.class.getSimpleName();

    //服务器路径
    private static final String API_SERVER = "http://zhuangbi.info";

    protected OkHttpClient mOkHttpClient;





    //okhttp3
    /**
     * 获取OkHttpClient对象
     *
     */
    protected   OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置一个自动管理cookies的管理器
                    // .cookieJar(new CookiesManager())
                    //添加拦截器
                    .addInterceptor(new MyIntercepter())
                    //添加网络连接器
                    //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getInstance().getApplicationContext()))
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
//                    .cache(cache)
                    .build();
        }

        return mOkHttpClient;
    }

    /**
     * 拦截器
     */
    private class MyIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            //ysl-----start----------------以下代码为添加一些公共参数使用--------------------------
            // 添加新的参数
            String time = System.currentTimeMillis()/1000 + "";
            HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
                    .addQueryParameter("os", "android")
                    .addQueryParameter("time", URLEncoder.encode(time, "UTF-8"));
            // 构建新的请求
            Request newRequest = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(authorizedUrlBuilder.build())
                    .build();

            Response response = chain.proceed(newRequest);
            //***************打印Log*****************************
            String requestUrl = newRequest.url().toString(); // 获取请求url地址
            String methodStr = newRequest.method(); // 获取请求方式
            RequestBody body = newRequest.body(); // 获取请求body
            String bodyStr = (body == null ? "" : body.toString());

            // 打印Request数据
            TLog.i(Tag, "requestUrl=====>" + requestUrl);
            TLog.i(Tag, "requestMethod=====>" + methodStr);
            TLog.i(Tag, "requestBody=====>" + body);
            return response;
        }
    }

    private String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }


//    /**
//     * 自动管理Cookies
//     */
//    private static class CookiesManager implements CookieJar {
//        private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getInstance().getApplicationContext());
//        //在接收时，读取response header中的cookie
//        @Override
//        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//            if (cookies != null && cookies.size() > 0) {
//                for (Cookie item : cookies) {
//                    cookieStore.add(url, item);
//                }
//            }else{
//                Log.i("ysl","cookie为null");
//            }
//        }
//        //分别是在发送时向request header中加入cookie
//        @Override
//        public List<Cookie> loadForRequest(HttpUrl url) {
//            Log.i("ysl","url为---" + url);
//            List<Cookie> cookies = cookieStore.get(url);
//            if (cookies.size() < 1) {
//                Log.i("ysl","cookies为null");
//            }
//            return cookies;
//        }
//    }


}
