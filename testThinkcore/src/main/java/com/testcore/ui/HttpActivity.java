package com.testcore.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.testcore.R;
import com.thinkcore.http.CoreHttpClient;
import com.thinkcore.ui.CoreAppActivity;
import com.thinkcore.utils.log.TLog;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HttpActivity extends CoreAppActivity implements View.OnClickListener {
    private String TAG = HttpActivity.class.getCanonicalName();
    private  String Html = "http://www.baidu.com";

    private TextView mResultTextView;
    private CoreHttpClient mClientHttp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        mResultTextView = (TextView) findViewById(R.id.TextView_result);

        findViewById(R.id.Button_asyn_get).setOnClickListener(this);
        findViewById(R.id.Button_syn_get).setOnClickListener(this);
        findViewById(R.id.Button_asyn_post).setOnClickListener(this);
        findViewById(R.id.Button_syn_post).setOnClickListener(this);


        mClientHttp = new CoreHttpClient();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Button_syn_get) {
//            okHttpSynchronousGet();
            getSynHttp();
        } else if (v.getId() == R.id.Button_asyn_get) {
            okHttpAsynchronousGet();
        } else if (v.getId() == R.id.Button_syn_post) {
            okHttpPostFromParameters();
        } else if (v.getId() == R.id.Button_asyn_post) {
            testRxJava();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setResult(final String result){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText("请求成功");
                mResultTextView.setText(result);
            }
        });
    }

    private void getSynHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder requestBuilder = new Request.Builder().url(Html);
                    //可以省略，默认是GET请求
                    requestBuilder.method("GET", null);
                    Request request = requestBuilder.build();
                    Response response = client.newCall(request).execute();

                    if (null != response.cacheResponse()) {
                        String str = response.cacheResponse().toString();
                        TLog.i("wangshu", "cache---" + str);
                    } else {
                        setResult(response.body().string());
                        String str = response.networkResponse().toString();
                        TLog.i("wangshu", "network---" + str);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 异步 Get方法
     */
    private void okHttpAsynchronousGet(){
        OkHttpClient client=new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(Html);
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= client.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    TLog.i("wangshu", "cache---" + str);
                } else {
                    setResult(response.body().string());
                    String str = response.networkResponse().toString();
                    TLog.i("wangshu", "network---" + str);
                }
            }
        });
    }


//    private final OkHttpClient client = new OkHttpClient();
//    public void run() throws Exception {
//        Request request = new Request.Builder()
//                .url("https://api.github.com/repos/square/okhttp/issues")
//                .header("User-Agent", "OkHttp Headers.java")
//                .addHeader("Accept", "application/json; q=0.5")
//                .addHeader("Accept", "application/vnd.github.v3+json")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        System.out.println("Server: " + response.header("Server"));
//        System.out.println("Date: " + response.header("Date"));
//        System.out.println("Vary: " + response.headers("Vary"));
//    }

    private void okHttpPostFromParameters() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 请求完整url：http://api.k780.com:88/?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String json = "{'app':'weather.future','weaid':'1','appkey':'10003'," +
                            "'sign':'b59bc3ef6191eb9f747dd4e83c99f2a4','format':'json'}";
                    RequestBody formBody = new FormBody.Builder().add("app", "weather.future")
                            .add("weaid", "1").add("appkey", "10003").add("sign",
                                    "b59bc3ef6191eb9f747dd4e83c99f2a4").add("format", "json")
                            .build();
                    Request request = new Request.Builder().url(Html).post(formBody).build();
                    okhttp3.Response response = okHttpClient.newCall(request).execute();
                    if(response.isSuccessful())
                        setResult(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //可以使用Post方法发送一串字符串，但不建议发送超过1M的文本信息，如下示例展示了，发送一个markdown文本
//    public static final MediaType MEDIA_TYPE_MARKDOWN
//            = MediaType.parse("text/x-markdown; charset=utf-8");
//    private final OkHttpClient client = new OkHttpClient();
//    public void run() throws Exception {
//        String postBody = ""
//                + "Releases\n"
//                + "--------\n"
//                + "\n"
//                + " * _1.0_ May 6, 2013\n"
//                + " * _1.1_ June 15, 2013\n"
//                + " * _1.2_ August 11, 2013\n";
//
//        Request request = new Request.Builder()
//                .url("https://api.github.com/markdown/raw")
//                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        System.out.println(response.body().string());
//    }

    //post可以将stream对象作为请求体，依赖以Okio 将数据写成输出流形式；
//    public static final MediaType MEDIA_TYPE_MARKDOWN
//            = MediaType.parse("text/x-markdown; charset=utf-8");
//    private final OkHttpClient client = new OkHttpClient();
//    public void run() throws Exception {
//        RequestBody requestBody = new RequestBody() {
//            @Override public MediaType contentType() {
//                return MEDIA_TYPE_MARKDOWN;
//            }
//
//            @Override public void writeTo(BufferedSink sink) throws IOException {
//                sink.writeUtf8("Numbers\n");
//                sink.writeUtf8("-------\n");
//                for (int i = 2; i <= 997; i++) {
//                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
//                }
//            }
//
//            private String factor(int n) {
//                for (int i = 2; i < n; i++) {
//                    int x = n / i;
//                    if (x * i == n) return factor(x) + " × " + i;
//                }
//                return Integer.toString(n);
//            }
//        };
//
//        Request request = new Request.Builder()
//                .url("https://api.github.com/markdown/raw")
//                .post(requestBody)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        System.out.println(response.body().string());
//    }


    //Post file
//    public static final MediaType MEDIA_TYPE_MARKDOWN
//            = MediaType.parse("text/x-markdown; charset=utf-8");
//
//    private final OkHttpClient client = new OkHttpClient();
//
//    public void run() throws Exception {
//        File file = new File("README.md");
//
//        Request request = new Request.Builder()
//                .url("https://api.github.com/markdown/raw")
//                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        System.out.println(response.body().string());
//    }


//    private void postAsynFile() {
//        mOkHttpClient=new OkHttpClient();
//        File file = new File("/sdcard/wangshu.txt");
//        Request request = new Request.Builder()
//                .url("https://api.github.com/markdown/raw")
//                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
//                .build();
//
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("wangshu",response.body().string());
//            }
//        });
//    }

    //http://blog.csdn.net/tangxl2008008/article/details/51730187
    private void testRxJava()
    {
//
        final OkHttpClient client = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Html)
                .build();
        rx.Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    subscriber.onStart();
                    //处理请求在IO线程中进行
                    final Response response = client.newCall(request).execute();
                    if (null != response.cacheResponse()) {
                        subscriber.onError(new Exception("无数据"));
                    } else {
                        String body = response.body().string();
                        subscriber.onNext(body);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
//                        showInfo(e.toString());
                        setResult(e.getMessage());
                    }
                    @Override
                    public void onNext(String s) {
                        //处理返回结果，在UI线程中，可以直接显示结果
//                        showInfo(s);
                        setResult(s);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                }) ;

    }
}
