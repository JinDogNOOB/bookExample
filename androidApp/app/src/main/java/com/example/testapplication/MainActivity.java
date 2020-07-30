package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.testapplication.util.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // 서버 IP 저장
        editor.putString("webAddress", "http://211.254.214.146:8546");
        editor.apply();

        // 로그인 관련 변수
        EditText idText = findViewById(R.id.a_main_id_text);
        EditText pwText = findViewById(R.id.a_main_passwd_text);
        Button gotoLegiButton = findViewById(R.id.a_main_legister_button);
        Button loginOkButton = findViewById(R.id.a_main_ok_button);



        // 회원가입 진행 ##########################################################################
        gotoLegiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_signup);

                EditText idText = dialog.findViewById(R.id.d_signup_id_text);
                EditText pwText = dialog.findViewById(R.id.d_signup_pw_text);
                EditText nameText = dialog.findViewById(R.id.d_signup_name_text);
                EditText addressText = dialog.findViewById(R.id.d_signup_address_text);

                RadioButton radioButton0 = dialog.findViewById(R.id.d_signup_radio0);
                RadioButton radioButton1 = dialog.findViewById(R.id.d_signup_radio1);
                RadioButton radioButton2 = dialog.findViewById(R.id.d_signup_radio2);
                RadioButton radioButton99 = dialog.findViewById(R.id.d_signup_radio99);

                Button okButton = dialog.findViewById(R.id.d_signup_ok_button);
                Button exButton = dialog.findViewById(R.id.d_signup_ex_button);


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int userType = 0;
                        if(radioButton0.isChecked()){
                            userType = 0;
                        }else if(radioButton1.isChecked()){
                            userType = 1;
                        }else if(radioButton2.isChecked()){
                            userType = 2;
                        }else if(radioButton99.isChecked()){
                            userType = 99;
                        }else{
                            Toast.makeText(MainActivity.this, "유저 타입을 선택해주세요", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userid", idText.getText().toString());
                        contentValues.put("password", pwText.getText().toString());
                        contentValues.put("usertype", userType);
                        contentValues.put("name", nameText.getText().toString());
                        contentValues.put("address", addressText.getText().toString());


                        SignupNetworkAsyncTask signupNetworkAsyncTask = new SignupNetworkAsyncTask();
                        signupNetworkAsyncTask.execute(contentValues);

                        dialog.dismiss();
                    }
                });




                exButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
            dialog.show();
            }
        });




        // 로그인 진행 ###############################################################################
        loginOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("userid", idText.getText().toString());
                contentValues.put("password", pwText.getText().toString());

                LoginNetworkAsyncTask loginNetworkAsyncTask = new LoginNetworkAsyncTask();
                loginNetworkAsyncTask.execute(contentValues);
            }
        });


    }



    public class SignupNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/user/signup");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(getApplicationContext(), "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }





    public class LoginNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/user/signin");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{
                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다", Toast.LENGTH_SHORT).show();
                    // jwt 토큰 저장
                    editor.putString("jwt", jsonObject.getString("jwt"));
                    editor.putInt("userType", Integer.valueOf(jsonObject.getString("usertype")));
                    editor.putString("key", jsonObject.getString("key"));

                    editor.apply();
                    // 허브 액티비티로 이동
                    Intent intent = new Intent(getApplicationContext(), HubActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }














}
