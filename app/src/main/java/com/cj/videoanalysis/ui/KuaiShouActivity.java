package com.cj.videoanalysis.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cj.videoanalysis.R;
import com.cj.videoanalysis.logger.LogUtils;
import com.cj.videoanalysis.tool.AsyncHttpUrl;
import com.cj.videoanalysis.tool.AsyncHttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * 作者：陈骏
 * 创建时间：2018/1/11. 9:47
 * QQ：200622550
 * 博客：https://www.jianshu.com/u/c5ada9939f6d
 * 个人网站：www.coocj.top
 * 作用：快手解析
 */

public class KuaiShouActivity extends AppCompatActivity {
    private TextView _td;
    private TextView _sm;
    private TextView _sy;

    private EditText editText;
    private TextView textView;
    private Button add;
    private Button clen;
    private Button download;
    private ImageView iv;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static DisplayImageOptions options;
    private static ImageLoader imageLoader;
    private String Cookie = "";
    private String Download = "";

        String test = "http://www.gifshow.com/i/photo/lwx?userId=698642431&photoId=4666434675&timestamp=1516324422149&cc=share_copylink&et=1_i%2F1589981365580087296_h85";
    static {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uitily);
        this.setTitle("快手解析");
        _td = (TextView) findViewById(R.id.tedian);
        _sm = (TextView) findViewById(R.id.tedian_shuoming);
        _sy = (TextView) findViewById(R.id.shiyong);
        _td.setText("快手小视频解析下载工具特点:");
        _sm.setText("1.支持解析任何快手视频\n2.解析出来的视频没有水印");
        _sy.setText("1.打开快手APP，点开某个视频，点击顶部分享按钮，在分享弹框中点击复制链接或通过分享到微信QQ等获取分享链接\n2.将刚才复制的链接粘贴到下面的输入框");
        imageLoader.init(ImageLoaderConfiguration.createDefault(KuaiShouActivity.this));
        editText = (EditText) findViewById(R.id.ed);
        iv = (ImageView) findViewById(R.id.iv);
        textView = (TextView) findViewById(R.id.tv);
        add = (Button) findViewById(R.id.add);
        clen = (Button) findViewById(R.id.clen);
        download = (Button) findViewById(R.id.download);
        editText.setText(test);
        AsyncHttpUrl.get(this, client, "http://kuaishou.iiilab.com/", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                PersistentCookieStore myCookieStore = new PersistentCookieStore(KuaiShouActivity.this);
                List<cz.msebera.android.httpclient.cookie.Cookie> cookies = myCookieStore.getCookies();
                for (Cookie cookie : cookies) {
                    Cookie = cookie.getValue();
                    LogUtils.d(cookie.getName() + " = " + cookie.getValue());
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

        clen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                iv.setVisibility(View.GONE);
                download.setVisibility(View.GONE);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    clen.setVisibility(View.GONE);
                    iv.setVisibility(View.GONE);
                    download.setVisibility(View.GONE);
                } else {
                    clen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("link", editText.getText().toString().trim());
                params.put("r", "7186453735009171");
                //params.put("r", (long) ((Math.random() * 9 + 1) * 1000000000000000L)+"");
                params.put("s", "3587036368");
                AsyncHttpUrl.postKuaiShou(KuaiShouActivity.this, client, Cookie, "http://service.iiilab.com/video/kuaishou", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.i("TAG", new String(bytes));
                        try {
                            JSONObject json = new JSONObject(new String(bytes));
                            String retDesc = json.getString("retDesc");
                            int retCode = json.getInt("retCode");
                            textView.setText(retDesc);
                            if (retCode == 200) {
                                JSONObject data = json.getJSONObject("data");
                                String url = data.getString("cover");
                                LogUtils.d(url);
                                imageLoader.displayImage(url, iv, options);
                                Download = data.getString("video");
                                download.setVisibility(View.VISIBLE);
                                iv.setVisibility(View.VISIBLE);
                            } else {
                                iv.setVisibility(View.GONE);
                                download.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        textView.setText("解析失败！");
                    }
                });
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncHttpUtil.downloadFile(KuaiShouActivity.this, Download, download, AsyncHttpUtil.HUOSHAN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}