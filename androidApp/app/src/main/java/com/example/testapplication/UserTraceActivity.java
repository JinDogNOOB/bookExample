package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.qr.HubToUserQr;
import com.example.testapplication.util.HttpConnection;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserTraceActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String sharedQrStr = "";

    TextView keyText;
    TextView statusText;
    TextView weightText;
    TextView descText;

    String cowPtrStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trace);

        Intent intent = getIntent();
        sharedQrStr = intent.getExtras().getString("qrData");

        HubToUserQr activity = (HubToUserQr)HubToUserQr.activity;
        activity.finish(); // 전에 중간 지점 HubToUser 액티비티를 종료

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        keyText = findViewById(R.id.a_usertrace_key_text);
        statusText = findViewById(R.id.a_usertrace_status_text);
        weightText = findViewById(R.id.a_usertrace_weight_text);
        descText = findViewById(R.id.a_usertrace_desc_text);



        ContentValues contentValues = new ContentValues();
        contentValues.put("jwt", sharedPreferences.getString("jwt", null));
        contentValues.put("meatptr", sharedQrStr);

        GetMeatNetworkAsyncTask getMeatNetworkAsyncTask = new GetMeatNetworkAsyncTask();
        getMeatNetworkAsyncTask.execute(contentValues);


        Button gotoListButton = findViewById(R.id.a_usertrace_gotolist_button);
        Button gotoCowInfoButton = findViewById(R.id.a_usertrace_cowinfo_button);
        Button boughtButton = findViewById(R.id.a_usertrace_bought_button);

        gotoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTraceActivity.this, UserTraceListActivity.class );
                intent.putExtra("meatptr",sharedQrStr);
                startActivity(intent);
            }
        });


        gotoCowInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent(UserTraceActivity.this, UserTraceCowInfoActivity.class);
                intent.putExtra("cowptr", cowPtrStr);
                startActivity(intent);

            }
        });

        boughtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("jwt", sharedPreferences.getString("jwt", null));
                cv.put("meatptr", keyText.getText().toString());

                ChangeMeatStatusToSelledNetworkAsyncTask changeMeatStatusToSelled = new ChangeMeatStatusToSelledNetworkAsyncTask();
                changeMeatStatusToSelled.execute(cv);
            }
        });


    }






    public class GetMeatNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/user/getmeat");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(UserTraceActivity.this, "success: 고기 정보 로드에 성공", Toast.LENGTH_SHORT).show();
                    keyText.setText(jsonObject.getString("key"));
                    statusText.setText(jsonObject.getString("status"));
                    weightText.setText(jsonObject.getString("weight"));
                    descText.setText(jsonObject.getString("desc"));

                    cowPtrStr = jsonObject.getString("cowPtr");

                }else{
                    Toast.makeText(UserTraceActivity.this, "fail: 고기 정보 로드에 실패", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(UserTraceActivity.this, "fail: 고기 정보 로드에 실패", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }

    public class ChangeMeatStatusToSelledNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/user/meatselled");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(UserTraceActivity.this, "success", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(UserTraceActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(UserTraceActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }



    public Bitmap getQrCode(String contents){
        System.out.println("QR STring : " + contents);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try{
            HashMap<EncodeHintType, String> hint = new HashMap<EncodeHintType, String>();
            hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 600, 600, hint));
            return bitmap;
        }catch (WriterException e){
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for( int x = 0 ; x < width ; x++){
            for(int y = 0; y < height; y++){
                bitmap.setPixel(x, y, matrix.get(x,y)? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }



}
