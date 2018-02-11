package com.functionlist.httpUpload;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.functionlist.R;
import com.functionlist.httpUpload.Utils.Constants;
import com.functionlist.httpUpload.Utils.Logger;
import com.functionlist.httpUpload.Utils.ThreadUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUploadActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_upload);
        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_head).setOnClickListener(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get:
                getRequest();
                break;
            case R.id.btn_head:
                headRequest();
                break;
            case R.id.btn_post:
                postRequest();
                break;
        }
    }

    /**
     * GET请求
     */
    public void getRequest() {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {

                    executeGet();

            }
        });
    }

    /**
     * 执行网络请求
     */
    private void executeGet()  {

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(Constants.URL_UPLOAD);
            //建立连接
            urlConnection = (HttpURLConnection) url.openConnection();
            if(urlConnection instanceof HttpsURLConnection){
                //https
            }
            urlConnection.setRequestMethod("GET");
            //发送数据
            urlConnection.connect();
            int responseCode =  urlConnection.getResponseCode();
            if(responseCode == 200){
                inputStream = urlConnection.getInputStream();
                readServerData(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * HEAD请求
     */
    public void headRequest() {

    }
    private void executeHead(){

    }
    /**
     * POST、PUT、DELETE
     */
    public void postRequest() {

    }
    private void executePost(){

    }
    private void readServerData(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer))!= -1){
            outputStream.write(buffer,0,len);
        }
        outputStream.close();
        inputStream.close();
        String data = new String(outputStream.toByteArray());
        Logger.i("响应数据" + data);
    }
}
