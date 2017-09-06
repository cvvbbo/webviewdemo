package webview.example.xz.mwebview;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import webview.example.xz.mwebview.utils.ImageUtil;
import webview.example.xz.mwebview.utils.PermissionUtil;

public class NewDetailActivity extends AppCompatActivity implements ChomeClient.OpenFileChooserCallBack {
    @BindView(R.id.rdbt1)
    RadioButton rdbt1;
    @BindView(R.id.rdbt2)
    RadioButton rdbt2;
    @BindView(R.id.rdbt3)
    RadioButton rdbt3;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.title)
    TextView title;
    //private String url;
    private ImageView ivBack;
    private ImageView ivFav;
    private ImageView ivShare;
    private ImageView ivTextSize;
    private WebView webView;
    private ImageView ivSpeak;
    private WebSettings settings;
    private String content;

    private boolean isExit;


    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;

    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    public ValueCallback<Uri[]> mUploadMsgForAndroid5;

    // permission Code
    private static final int P_CODE_PERMISSIONS = 101;


    private String[] str = new String[]{"超大", "大号", "普通", "小号", "极小"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener


        setContentView(R.layout.activity_new_detail);
        ButterKnife.bind(this);

        //ButterKnife  黄油刀

        webView = (WebView) findViewById(R.id.webView);


        //webView加载url  html

        //http://www.12306.cn/mormhweb/  给pc机器浏览器使用网页
        checkone("https://www.emiaoqian.com/");
        //radiogroup.check(R.id.rdbt1);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rdbt1:
                        checkone("http://192.168.1.108/");
                        //"https://www.emiaoqian.com/"
                        title.setText("首页");
                        break;
                    case R.id.rdbt2:
                        // "https://www.emiaoqian.com/sys/waybill/quick"
                        checkone("http://192.168.1.108/sys/waybill/quick");
                        title.setText("我要寄件");
                        break;
                    case R.id.rdbt3:
                        //"https://www.emiaoqian.com/sys/pub/login"
                        checkone("http://192.168.1.108/sys");
                        title.setText("工作台");
                        break;
                }
            }
        });

    }

    private void checkone(String url) {
        webView.loadUrl(url);
        //将+号和-号的按钮显示出来,供放大缩小
        settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setUseWideViewPort(true);//出现+,-号放大缩小,在wap网页上无效
        //网页前端 html5  css js jquery  指定js可用
        settings.setJavaScriptEnabled(true);//wap网页

        webView.setWebChromeClient(new ChomeClient(this));

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        //使网页端能够识别是什么客户端使用了代理
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua.replace("Android", "emiaoqian Android"));


        //让网页确保在webView中开启
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }


            //在本应用中开启网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }
            }

        });

        fixDirPath();

    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void checktwo() {
        webView.loadUrl("https://www.emiaoqian.com/sys/waybill/quick");
        //将+号和-号的按钮显示出来,供放大缩小
        settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setUseWideViewPort(true);//出现+,-号放大缩小,在wap网页上无效
        //网页前端 html5  css js jquery  指定js可用
        settings.setJavaScriptEnabled(true);//wap网页

        // webView.setWebChromeClient(new MyWebChromeClient());

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //让网页确保在webView中开启
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    private void checkthree() {
        webView.loadUrl("https://www.emiaoqian.com/sys/pub/login");
        //将+号和-号的按钮显示出来,供放大缩小
        settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);//出现+,-号放大缩小,在wap网页上无效
        settings.setUseWideViewPort(true);//出现+,-号放大缩小,在wap网页上无效
        //网页前端 html5  css js jquery  指定js可用
        settings.setJavaScriptEnabled(true);//wap网页

        // webView.setWebChromeClient(new MyWebChromeClient());

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //让网页确保在webView中开启
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }


    protected void showTextSizeDialog() {
        Builder builder = new Builder(this);
        builder.setTitle("修改文本字体大小");
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
//					settings.setTextSize(TextSize.LARGEST);
                        settings.setTextZoom(200);
                        break;
                    case 1:
//					settings.setTextSize(TextSize.LARGER);
                        settings.setTextZoom(150);
                        break;
                    case 2:
//					settings.setTextSize(TextSize.NORMAL);
                        settings.setTextZoom(100);
                        break;
                    case 3:
//					settings.setTextSize(TextSize.SMALLER);
                        settings.setTextZoom(75);
                        break;
                    case 4:
//					settings.setTextSize(TextSize.SMALLEST);
                        settings.setTextZoom(50);
                        break;
                }
            }
        });

        builder.show();
    }


    public void onBackPressed() {
        if (isExit) {
            this.finish();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            isExit = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }
    }

    //点击照片之后还能返回上一个页面！！
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * 这里监控的是物理返回或者设置了该接口的点击事件
         * 当按钮事件为返回时，且WebView可以返回，即触发返回事件
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack())
                webView.goBack(); // goBack()表示返回WebView的上一页面
            else
                NewDetailActivity.this.finish();
            return true; // 返回true拦截事件的传递
        }
        return false;
    }


    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        showOptions();

    }

    @Override
    public boolean openFileChooserCallBackAndroid5(WebView webView, ValueCallback<Uri[]>
            filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

        mUploadMsgForAndroid5 = filePathCallback;
        showOptions();

        return true;
    }

    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setOnCancelListener(new DialogOnCancelListener());

        alertDialog.setTitle("请选择操作");
        // gallery, camera.
        String[] options = {"相册", "拍照"};

        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(NewDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    Toast.makeText(NewDetailActivity.this,
                                            "请去\"设置\"中开启本应用的图片媒体访问权限",
                                            Toast.LENGTH_SHORT).show();

                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                            }

                            try {
                                mSourceIntent = ImageUtil.choosePicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(NewDetailActivity.this,
                                        "请去\"设置\"中开启本应用的图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();
                                restoreUploadMsg();
                            }

                        } else {
                            if (PermissionUtil.isOverMarshmallow()) {
                                if (!PermissionUtil.isPermissionValid(NewDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    Toast.makeText(NewDetailActivity.this,
                                            "请去\"设置\"中开启本应用的图片媒体访问权限",
                                            Toast.LENGTH_SHORT).show();

                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }

                                if (!PermissionUtil.isPermissionValid(NewDetailActivity.this, Manifest.permission.CAMERA)) {
                                    Toast.makeText(NewDetailActivity.this,
                                            "请去\"设置\"中开启本应用的相机权限",
                                            Toast.LENGTH_SHORT).show();

                                    restoreUploadMsg();
                                    requestPermissionsAndroidM();
                                    return;
                                }
                            }

                            try {
                                mSourceIntent = ImageUtil.takeBigPicture();
                                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(NewDetailActivity.this,
                                        "请去\"设置\"中开启本应用的相机和图片媒体访问权限",
                                        Toast.LENGTH_SHORT).show();

                                restoreUploadMsg();
                            }
                        }
                    }
                }
        );

        alertDialog.show();
    }

    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    private void restoreUploadMsg() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;

        } else if (mUploadMsgForAndroid5 != null) {
            mUploadMsgForAndroid5.onReceiveValue(null);
            mUploadMsgForAndroid5 = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case P_CODE_PERMISSIONS:
                requestResult(permissions, grantResults);
                restoreUploadMsg();
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermissionsAndroidM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissionList = new ArrayList<>();
            needPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.CAMERA);

            PermissionUtil.requestPermissions(NewDetailActivity.this, P_CODE_PERMISSIONS, needPermissionList);

        } else {
            return;
        }
    }

    public void requestResult(String[] permissions, int[] grantResults) {
        ArrayList<String> needPermissions = new ArrayList<String>();

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtil.isOverMarshmallow()) {

                    needPermissions.add(permissions[i]);
                }
            }
        }

        if (needPermissions.size() > 0) {
            StringBuilder permissionsMsg = new StringBuilder();

            for (int i = 0; i < needPermissions.size(); i++) {
                String strPermissons = needPermissions.get(i);

                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append(",文件");

                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append(",文件");

                } else if (Manifest.permission.CAMERA.equals(strPermissons)) {
                    permissionsMsg.append(",摄像头");

                }
            }

            String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";

            Toast.makeText(NewDetailActivity.this, strMessage, Toast.LENGTH_SHORT).show();

        } else {
            return;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
            }

            if (mUploadMsgForAndroid5 != null) {         // for android 5.0+
                mUploadMsgForAndroid5.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsg == null) {
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsg.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);

                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
