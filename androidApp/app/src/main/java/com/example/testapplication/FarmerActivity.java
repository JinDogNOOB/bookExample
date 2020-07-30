package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.util.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FarmerActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    TextView cowTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        cowTextView = findViewById(R.id.a_farmer_cows_text);

        ContentValues contentValues = new ContentValues();
        contentValues.put("jwt", sharedPreferences.getString("jwt", null));

        CowListNetworkAsyncTask cowListNetworkAsyncTask = new CowListNetworkAsyncTask();
        cowListNetworkAsyncTask.execute(contentValues);


    }


    public class CowListNetworkAsyncTask extends AsyncTask<ContentValues, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(ContentValues... contentValues) {
            HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/buser/getMyCow");
            return httpConnection.request(contentValues[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try{

                if(jsonObject.getBoolean("resultCode") == true){
                    Toast.makeText(FarmerActivity.this, "성공했습니다", Toast.LENGTH_SHORT).show();

                    cowTextView.setText(jsonObject.toString());

                }else{
                    Toast.makeText(FarmerActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(FarmerActivity.this, "실패했습니다", Toast.LENGTH_SHORT).show();
            }
            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }
}
