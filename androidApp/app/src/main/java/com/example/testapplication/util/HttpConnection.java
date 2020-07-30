package com.example.testapplication.util;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpConnection {

    String urlSTr = "";

    public HttpConnection(String urlStr){
        this.urlSTr = urlStr;
    }


    public JSONObject request(ContentValues _params){

        HttpURLConnection httpURLConnection = null;
        StringBuffer sbParams = new StringBuffer();


        if(_params == null){
            sbParams.append("");
        }else{
            boolean isAnd = false;

            // 파라미터 키와 값
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();
                if(isAnd)
                    sbParams.append("&");
                sbParams.append(key).append("=").append(value);

                if(!isAnd)
                    if(_params.size() >= 2)
                        isAnd = true;
            }
        }


        System.out.println("aaa"+ sbParams.toString());



        try{
            URL url = new URL(urlSTr);
            httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8"); // accept charset 설정
            httpURLConnection.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            String strParams = sbParams.toString();
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if(httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));

            String jsonText = readAll(reader);
            JSONObject jsonObject = new JSONObject(jsonText);
            System.out.println("xx " + jsonObject.toString());
            return jsonObject;

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }finally{
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return null;
    }


    private String readAll(BufferedReader reader) {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        try {
            while ((cp = reader.read()) != -1) {
                stringBuilder.append((char) cp);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }
}
