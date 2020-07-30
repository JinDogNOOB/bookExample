package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapplication.dto.Meat;
import com.example.testapplication.util.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserTraceListActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String meatPtrStr;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trace_list);

        sharedPreferences = getSharedPreferences("A", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        listView = findViewById(R.id.a_usertracelist_listview);

        Intent intent = getIntent();
        meatPtrStr = intent.getExtras().getString("meatptr");

        ContentValues contentValues = new ContentValues();
        contentValues.put("jwt", sharedPreferences.getString("jwt", null));
        contentValues.put("meatptr", meatPtrStr);

        GetMeatListNetworkAsyncTask getMeatListNetworkAsyncTask = new GetMeatListNetworkAsyncTask();
        getMeatListNetworkAsyncTask.execute(contentValues);



    }



    public class GetMeatListNetworkAsyncTask extends AsyncTask<ContentValues, Void, ArrayList<Meat>> {

        JSONObject result;

        @Override
        protected  ArrayList<Meat> doInBackground(ContentValues... contentValues) {
            ArrayList<Meat> arrayList = new ArrayList<Meat>();

            try{
                Meat meatVO;
                HttpConnection httpConnection = new HttpConnection(sharedPreferences.getString("webAddress", null) + "/user/getmeat");
                result = httpConnection.request(contentValues[0]);

                meatVO = new Meat();
                meatVO.setStatus(result.getInt("status"));
                meatVO.setCowPtr(result.getString("cowPtr"));
                meatVO.setDesc(result.getString("desc"));
                meatVO.setKey(result.getString("key"));
                meatVO.setpMeatPtr(result.getString("pMeatPtr"));
                meatVO.setWeight(result.getInt("weight"));
                meatVO.setWorkerPtr(result.getString("workerPtr"));

                arrayList.add(meatVO);

                for(int i = 1; i < 100;i++) {
                    contentValues[0].clear();
                    contentValues[0].put("jwt", sharedPreferences.getString("jwt", null));
                    contentValues[0].put("meatptr", arrayList.get(i-1).getpMeatPtr());

                    result = httpConnection.request(contentValues[0]);
                    if(!result.getBoolean("resultCode")){
                        break;
                    }
                    Meat meatVOf = new Meat();
                    meatVOf.setStatus(result.getInt("status"));
                    meatVOf.setCowPtr(result.getString("cowPtr"));
                    meatVOf.setDesc(result.getString("desc"));
                    meatVOf.setKey(result.getString("key"));
                    meatVOf.setpMeatPtr(result.getString("pMeatPtr"));
                    meatVOf.setWeight(result.getInt("weight"));
                    meatVOf.setWorkerPtr(result.getString("workerPtr"));

                    arrayList.add(meatVOf);

                    System.out.println("meatVOadsfadfadfxxxxxxxxxxx" + meatVO.toString());
                }

                return arrayList;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(ArrayList<Meat> array) {
            super.onPostExecute(array);

            if(array == null){
                Toast.makeText(UserTraceListActivity.this, "이력이 없음" + array.toString(), Toast.LENGTH_SHORT).show();

            }else{
                System.out.println("ARRAY LENGTH XXX" + array.size());
                MeatListAdapter meatListAdapter = new MeatListAdapter(UserTraceListActivity.this, array);
                listView.setAdapter(meatListAdapter);
            }

            /*
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        }
    }



    public class MeatListAdapter extends BaseAdapter {
        Context context;
        ArrayList<Meat> items;

        public MeatListAdapter(Context mContext, ArrayList<Meat> mItems) {
            context = mContext;
            items = mItems;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_trace_list, null);
            }

            TextView meatKeyText = convertView.findViewById(R.id.lv_tracelist_meatkey_text);
            TextView workerText = convertView.findViewById(R.id.lv_tracelist_worker_text);
            TextView descText = convertView.findViewById(R.id.lv_tracelist_desc_text);



            meatKeyText.setText(items.get(position).getKey());
            workerText.setText(items.get(position).getWorkerPtr());
            descText.setText(items.get(position).getDesc());

            return convertView;
        }


    }
}
