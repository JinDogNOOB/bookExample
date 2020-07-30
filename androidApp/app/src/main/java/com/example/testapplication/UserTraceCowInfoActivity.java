package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.util.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class UserTraceCowInfoActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    TextView statusTextView;
    TextView descTextView;
    TextView ownerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trace_cowinfo);

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent intent = getIntent();
        String cowPtr = intent.getExtras().getString("cowptr");

        statusTextView = findViewById(R.id.a_usertracecowinfo_status_text);
        descTextView = findViewById(R.id.a_usertracecowinfo_desc_text);
        ownerTextView = findViewById(R.id.a_usertracecowinfo_ownerptr_text);

        ContentValues contentValues = new ContentValues();
        contentValues.put("cowptr", cowPtr);
        contentValues.put("jwt", sharedPreferences.getString("jwt", null));


        GetCowNetworkAsyncTask getCowNetworkAsyncTask = new GetCowNetworkAsyncTask();
        getCowNetworkAsyncTask.execute(contentValues);


    }

    public class GetCowNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/user/getcow");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(UserTraceCowInfoActivity.this, "success: 소 정보 로드에 성공", Toast.LENGTH_SHORT).show();
                    statusTextView.setText(jsonObject.getString("status"));
                    descTextView.setText(jsonObject.getString("desc"));
                    ownerTextView.setText(jsonObject.getString("ownerPtr"));

                }else{
                    Toast.makeText(UserTraceCowInfoActivity.this, "fail: 소 정보 로드에 성공", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(UserTraceCowInfoActivity.this, "fail: 소 정보 로드에 성공", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }
}
